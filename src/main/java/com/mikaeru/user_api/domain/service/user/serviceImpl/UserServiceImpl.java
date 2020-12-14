package com.mikaeru.user_api.domain.service.user.serviceImpl;

import com.mikaeru.user_api.domain.exception.DomainException;
import com.mikaeru.user_api.domain.exception.DuplicatedDataException;
import com.mikaeru.user_api.domain.model.phone.Phone;
import com.mikaeru.user_api.domain.model.role.Role;
import com.mikaeru.user_api.domain.model.user.User;
import com.mikaeru.user_api.domain.service.phone.PhoneService;
import com.mikaeru.user_api.domain.service.user.UserService;
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

    @Autowired private UserRepository userRepository;

    private static final String SORT_BY = "firstname";

    private static final String ROLE_USER = "ROLE_USER";

    private static final String ROLE_NOT_FOUND = "Role não encontrado!";
    private static final String INVALID_FIELDS = "Campos inválidos!";
    private static final String USER_NOT_FOUND = "Usuário não encontrado!";

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

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

        user.setCreatedAt(OffsetDateTime.now());
        user.setPassword(encoder.encode(user.getPassword()));

        try {
            user = userRepository.save(user);

            if (phones.size() > 0) {

                for (Phone phone: phones) {
                    phone.setOwner(user);
                }

                user.setPhones(phoneService.saveAll(phones));

                user = userRepository.save(user);
            }

        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException(INVALID_FIELDS);
        }

        if (insertDefaultAccess) {

            Optional<Role> authority = roleRepository.findByAuthority(ROLE_USER);

            if (authority.isEmpty()) {
                Role role = new Role();

                role.setAuthority(ROLE_USER);

                roleRepository.save(role);
            }

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

        if (user.getUsername() == null) {
            user.setUsername(userOptional.get().getUsername());
        }

        if (user.getPassword() == null) {
            user.setPassword(userOptional.get().getPassword());
        } else {
            user.setPassword(encoder.encode(user.getPassword()));
        }

        if (user.getEmail() == null) {
            user.setEmail(userOptional.get().getEmail());
        }

        if (user.getLastname() == null) {
            user.setLastname(userOptional.get().getLastname());
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
    public Page<User> findAllPages(Integer page, Integer itemsPerPage) {
        PageRequest pageRequest = PageRequest.of(page, itemsPerPage, Sort.by(SORT_BY));

        Page<User> userPage = userRepository.findAll(pageRequest);

        userPage.forEach(user -> {
            if (user.getPhones().size() == 0) user.setPhones(null);
        });

        return userPage;
    }

    @Override
    public List<User> findAllByName(String firstname) {

        List<User> users = userRepository.findAllByFirstname(firstname);

        users.forEach(user -> {
            if (user.getPhones().size() == 0) user.setPhones(null);
        });

        return users;
    }

    @Override
    public Page<User> findAllByName(Integer page, Integer itemsPerPage, String firstname) {

        PageRequest pageRequest;

        Page<User> outputPage;

        if (firstname == null || firstname.isEmpty() || firstname.equalsIgnoreCase("undefined")) {

            pageRequest = PageRequest.of(page, itemsPerPage, Sort.by(SORT_BY));

            outputPage = userRepository.findAll(pageRequest);

        } else {

            pageRequest = PageRequest.of(page, itemsPerPage, Sort.by(SORT_BY));

            outputPage = userRepository.findByUsernamePage(firstname, pageRequest);
        }

        outputPage.forEach(user -> {
            if (user.getPhones().size() == 0) user.setPhones(null);
        });

        return outputPage;
    }

    @Override
    public User findByExternalId(UUID externalId) {

        Optional<User> user = userRepository.findByExternalId(externalId);

        if (user.isEmpty()) {
            throw new EntityNotFoundException(USER_NOT_FOUND);
        }

        if (user.get().getPhones().size() == 0) user.get().setPhones(null);

        return user.get();
    }

    private void insertDefaultAccess(Long id) {

        String constraint = userRepository.searchConstraintRole();

       if (constraint != null) {
           jdbcTemplate.execute("ALTER TABLE user_role DROP CONSTRAINT " + constraint);
       }

        jdbcTemplate.execute("INSERT INTO user_role (user_id, role_id) VALUES(" +
                id +", (SELECT id FROM role_entity WHERE authority = '" + ROLE_USER + "'))");
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

    private void duplicatedFields(User user, User userInDatabase) {

        StringBuilder duplicatedField = new StringBuilder();

        if (userInDatabase.getId().equals(user.getId())) {
            duplicatedField.append("id");
        }

        if (userInDatabase.getUsername().equals(user.getUsername())) {
            duplicatedField.append("|username");
        }

        if (userInDatabase.getEmail().equals(user.getEmail())) {
            duplicatedField.append("|email");
        }

        if (user.getPhones() != null && user.getPhones().size() > 0) {

            int cont = 0;

            for (Phone phone: user.getPhones()) {
                cont += 1;
                for (Phone phoneInDatabase: userInDatabase.getPhones()) {

                    if (phone.getNumber().equals(phoneInDatabase.getNumber())) {
                        duplicatedField.append("|phone: ").append(cont);
                    }
                }
            }
        }

        if (duplicatedField.length() > 0) {

            DomainException.Error error = DomainException.Error.INVALID_DUPLICATED_DATA;

            error.setFields(DomainException.Error.convertToFieldList(duplicatedField.toString()));

            throw new DuplicatedDataException(error);
        }
    }
}
