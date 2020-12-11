package com.mikaeru.user_api.domain.service.user.serviceImpl;

import com.mikaeru.user_api.domain.exception.BusinessException;
import com.mikaeru.user_api.domain.model.phone.Phone;
import com.mikaeru.user_api.domain.model.user.User;
import com.mikaeru.user_api.domain.service.phone.CrudPhoneService;
import com.mikaeru.user_api.domain.service.user.UserService;
import com.mikaeru.user_api.dto.response.user.UserChart;
import com.mikaeru.user_api.repository.PhoneRepository;
import com.mikaeru.user_api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CrudPhoneService phoneService;

    @Autowired
    private PhoneRepository phoneRepository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public User saveUser(User user) {
        User userSaved = null;
        user.setCreated(OffsetDateTime.now());
        user.setPassword(encoder.encode(user.getPassword()));

        try {
            if (user.getPhones() == null) {
                userSaved = userRepository.save(user);
            } else {
                List<Phone> phones = user.getPhones();
                user.setPhones(null);
                userSaved = userRepository.save(user);

                for (Phone phone : phones) {
                    phone.setOwner(userSaved);
                }

                phones = phoneService.saveAllPhone(phones);
                userSaved.setPhones(phones);
                userSaved = userRepository.save(userSaved);
            }
        } catch (BusinessException e) {
            e.printStackTrace();
            throw new BusinessException("Error when trying to save the user");
        }

        insertDefaultAccess(userSaved.getId());

        return userSaved;
    }

    @Override
    public User updateUser(User user, List<Phone> phonesSaved) {

        List<Phone> phones = new ArrayList<>(user.getPhones());
        user.setPhones(null);

        for (Phone phone : phones) {
            phone.setOwner(user);
        }

        phones = phoneService.saveAllPhone(phones);
        phones.addAll(phonesSaved);
        user.setPhones(phones);

        user.setUpdated(OffsetDateTime.now());

        if (user.getPassword() != null)
            user.setPassword(encoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    @Override
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public void deleteUserById(Long id, User user) {
        phoneRepository.deleteAll(user.getPhones());
        userRepository.deleteById(id);
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

    private void insertDefaultAccess(Long id) {
        var constraint = userRepository.searchConstraintRole();
        var constraintUK = userRepository.searchConstraintRoleUK();

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
