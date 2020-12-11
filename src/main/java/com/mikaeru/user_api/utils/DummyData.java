package com.mikaeru.user_api.utils;

import com.mikaeru.user_api.domain.model.user.User;
import com.mikaeru.user_api.domain.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

//@Component
public class DummyData {

    private static final int NUMBER_OF_INSERTS = 12;

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder encoder;

//    @PostConstruct
    private void saveUsers() {
        List<User> users = new ArrayList<>();
        User user = null;
        for (int i =1; i <= NUMBER_OF_INSERTS; i++) {
            user = new User();
            user.setName("Insert" + i);
            user.setLastName("last" + i);
            user.setUsername("user" + i);
            user.setPassword(encoder.encode("123"));
            user.setCreated(OffsetDateTime.now());
            user.setEmail(user.getName() + "@gmail.com");
            user.setUuid(UUID.randomUUID());
            users.add(user);
        }

        for (User userTmp : users) {
            System.out.println(userService.saveUser(userTmp).getName());
        }
    }
}
