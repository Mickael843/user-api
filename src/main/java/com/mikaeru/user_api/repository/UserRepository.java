package com.mikaeru.user_api.repository;

import com.mikaeru.user_api.domain.model.user.User;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByExternalId(UUID externalId);

    Optional<User> findByUsername(String username);

    @Modifying
    @Transactional
    @Query(value = "update user_entity set user_password = ?1 where id = ?2", nativeQuery = true)
    void  updatePassword(String password, Long id);

    @Query(value = "select constraint_name from information_schema.constraint_column_usage where " +
            "table_name = 'user_role' and column_name = 'role_id' and constraint_name <>'uk_user_role'", nativeQuery = true)
    String searchConstraintRole();

    default Page<User> findByUsernamePage(String name, PageRequest pageRequest) {
        User user = new User();
        user.setFirstname(name);

        ExampleMatcher matcher = ExampleMatcher.matchingAny()
                .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());

        Example<User> example = Example.of(user, matcher);

        return findAll(example, pageRequest);
    }

    List<User> findAllByFirstname(String firstname);
}
