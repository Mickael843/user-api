package com.mikaeru.user_api.domain.service.user.serviceImpl;

import com.mikaeru.user_api.domain.model.user.User;
import com.mikaeru.user_api.domain.service.phone.PhoneService;
import com.mikaeru.user_api.domain.service.user.UserService;
import com.mikaeru.user_api.dto.user.out.UserChart;
import com.mikaeru.user_api.repository.PhoneRepository;
import com.mikaeru.user_api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private static final String USER_NOT_FOUND = "Usuário não encontrado!";

    @Autowired private JdbcTemplate jdbcTemplate;

    @Autowired private UserRepository userRepository;

    @Autowired private PhoneService phoneService;

    @Autowired private BCryptPasswordEncoder encoder;

    @Autowired private PhoneRepository phoneRepository;

    @Autowired private UserDetailsServiceImpl userDetailsService;

    @Override
    public User save(User user) {
        // TODO Implementar o método SAVE
        return null;
    }

    @Override
    public void update(User user) {
        // TODO Implementar o método UPDATE
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
}
