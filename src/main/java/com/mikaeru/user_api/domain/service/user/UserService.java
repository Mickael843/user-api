package com.mikaeru.user_api.domain.service.user;

import com.mikaeru.user_api.domain.model.phone.Phone;
import com.mikaeru.user_api.domain.model.user.User;
import com.mikaeru.user_api.dto.response.user.UserChart;

import java.util.List;

public interface UserService {

    User saveUser(User user);

    User updateUser(User user, List<Phone> phones);

    void deleteUserById(Long id);

    void deleteUserById(Long id, User user);

    void updatePassword(String password, Long idUser);

    UserChart getUserChart();
}
