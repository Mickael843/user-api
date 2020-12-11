package com.mikaeru.user_api.domain.service.user.serviceImpl;

import com.mikaeru.user_api.domain.model.phone.Phone;
import com.mikaeru.user_api.domain.model.role.Role;
import com.mikaeru.user_api.domain.model.user.User;
import com.mikaeru.user_api.domain.service.phone.PhoneService;
import com.mikaeru.user_api.domain.service.user.UserService;
import com.mikaeru.user_api.dto.user.out.UserChart;
import com.mikaeru.user_api.repository.RoleRepository;
import com.mikaeru.user_api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired private JdbcTemplate jdbcTemplate;

    @Autowired private PhoneService phoneService;

    @Autowired private RoleRepository roleRepository;

    @Autowired private BCryptPasswordEncoder encoder;

    @Autowired private UserRepository userRepository;

    private static final String INVALID_FIELDS = "Campos inválidos!";
    private static final String ROLE_NOT_FOUND = "Role não encontrado!";
    private static final String USER_NOT_FOUND = "Usuário não encontrado!";

    @Override
    public User save(User user) {

        boolean insertDefaultAccess = true;

        userValidation(user);

        List<Phone> phones = new ArrayList<>();

        if (user.getPhones() != null) {
            phones.addAll(user.getPhones());
            user.setPhones(null);
        }

        if (user.getRoles() != null) {

            List<Role> authorities = new ArrayList<>();

            insertDefaultAccess = false;

            for (Role authority: user.getRoles()) {
                Optional<Role> role = roleRepository.findByAuthority(authority.getAuthority());

                if (role.isEmpty()) {
                    throw new EntityNotFoundException(ROLE_NOT_FOUND);
                }

                authorities.add(role.get());
            }

            user.setRoles(authorities);
        }

        try {
            user = userRepository.save(user);

            if (phones.size() > 0) {

                for (Phone phone: phones) {
                    phone.setOwner(user);
                }

                phones = phoneService.saveAll(phones);
            }

            user.setPhones(phones);

            user = userRepository.save(user);

        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException(INVALID_FIELDS);
        }

        if (insertDefaultAccess) {
            insertDefaultAccess(user.getId());
        }

        return user;
    }

    @Override
    public void update(User user) {

        Optional<User> userOptional = userRepository.findByExternalId(user.getExternalId());

        if (userOptional.isEmpty()) {
            throw new EntityNotFoundException(USER_NOT_FOUND);
        }

        user.setId(userOptional.get().getId());
        user.setUpdatedAt(OffsetDateTime.now());
        user.setCreatedAt(userOptional.get().getCreatedAt());

        List<Role> authorities = new ArrayList<>(userOptional.get().getRoles());

        if (user.getUsername().isBlank()) {
            user.setUsername(userOptional.get().getUsername());
        }

        if (user.getPassword().isBlank()) {
            user.setPassword(userOptional.get().getPassword());
        } else {
            user.setPassword(encoder.encode(user.getPassword()));
        }

        if (user.getEmail().isBlank()) {
            user.setEmail(userOptional.get().getEmail());
        }

        if (user.getLastName().isBlank()) {
            user.setLastName(userOptional.get().getLastName());
        }

        if (user.getPhones() == null && userOptional.get().getPhones() != null) {
            user.setPhones(userOptional.get().getPhones());
        } else {

            for (Phone phone: user.getPhones()) {
                phone.setOwner(user);
            }

            user.setPhones(phoneService.saveAll(user.getPhones()));
        }

        if (user.getRoles() != null) {

            for (Role authority: user.getRoles()) {
                Optional<Role> role = roleRepository.findByAuthority(authority.getAuthority());

                if (role.isEmpty()) {
                    throw new EntityNotFoundException(ROLE_NOT_FOUND);
                }

                authorities.add(role.get());
            }

            user.setRoles(authorities);
        }

        userValidation(user);

        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException(INVALID_FIELDS);
        }
    }

    @Override
    public void delete(UUID externalId) {
        Optional<User> user = userRepository.findByExternalId(externalId);

        if (user.isEmpty()) {
            throw new EntityNotFoundException(USER_NOT_FOUND);
        }

        userRepository.delete(user.get());
    }

    @Override
    public void updatePassword(String password, Long id) {
        userRepository.updatePassword(password, id);
    }

    @Override
    public UserChart getUserChart() {

        UserChart userChart = new UserChart();

        String sql = "select array_agg(name_user) from user_entity where salary > 0 and " +
                "name_user <> '' union all select cast(array_agg(salary) as character varying[]) " +
                "from user_entity where salary > 0 and name_user <> ''";

        List<String> result = jdbcTemplate.queryForList(sql, String.class);

        if (!result.isEmpty()) {

            String names = result.get(0).replaceAll("\\{", "").replaceAll("}", "");

            String salaries = result.get(1).replaceAll("\\{", "").replaceAll("}", "");

            userChart.setName(names);

            userChart.setSalary(salaries);

            return userChart;
        }

        return userChart;
    }

    @Override
    public Page<User> findAllPages(Integer page, Integer itemsPerPage) {
        PageRequest pageRequest = PageRequest.of(page, itemsPerPage, Sort.by("name"));
        return userRepository.findAll(pageRequest);
    }

    @Override
    public List<User> findAllByName(String firstname) {
        return userRepository.findAllByFirstname(firstname);
    }

    @Override
    public Page<User> findAllByName(Integer page, Integer itemsPerPage, String firstname) {

        PageRequest pageRequest;

        Page<User> outputPage;

        if (firstname == null || firstname.isEmpty() || firstname.equalsIgnoreCase("undefined")) {

            pageRequest = PageRequest.of(page, itemsPerPage, Sort.by("name"));

            outputPage = userRepository.findAll(pageRequest);

        } else {

            pageRequest = PageRequest.of(page, itemsPerPage, Sort.by("name"));

            outputPage = userRepository.findByUsernamePage(firstname, pageRequest);
        }

        return outputPage;
    }

    @Override
    public User findByExternalId(UUID externalId) {

        Optional<User> user = userRepository.findByExternalId(externalId);

        if (user.isEmpty()) {
            throw new EntityNotFoundException(USER_NOT_FOUND);
        }

        return user.get();
    }

    private void insertDefaultAccess(Long id) {

        String constraint = userRepository.searchConstraintRole();

        String constraintUK = userRepository.searchConstraintRoleUK();

       if (constraint != null) {
           jdbcTemplate.execute("alter table user_entity_role drop constraint " + constraint);
       }

       if (constraintUK != null) {
           jdbcTemplate.execute("alter table user_entity_role drop constraint " + constraintUK);
       }

        jdbcTemplate.execute("insert into user_entity_role (user_entity_id, role_id) values(" +
                id +", (select id from role where authority = 'ROLE_USER'))");
    }

    private void userValidation(User user) {

        if (user.getExternalId() == null || user.getFirstname().isBlank()) {
            throw new DataIntegrityViolationException(INVALID_FIELDS);
        }

        // Somente para o método CREATE
        if (user.getId() == null) {

            if (user.getUsername().isEmpty() || user.getPassword().isEmpty()) {
                throw new DataIntegrityViolationException(INVALID_FIELDS);
            }
        }
    }
}
