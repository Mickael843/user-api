package com.mikaeru.user_api.domain.service.user;

import com.mikaeru.user_api.domain.model.user.User;
import com.mikaeru.user_api.dto.user.out.UserChart;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface UserService {

    User save(User user);

    void update(User user);

    void delete(UUID externalId);

    void updatePassword(String password, Long idUser);

    Page<User> findAllPages(Integer page, Integer itemsPerPage);

    List<User> findAllByName(String firstname);

    Page<User> findAllByName(Integer page, Integer itemsPerPage, String firstname);

    User findByExternalId(UUID externalId);
}
