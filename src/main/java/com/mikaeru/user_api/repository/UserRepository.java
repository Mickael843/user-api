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

    @Query("select u from User u where u.name like %?1%")
    List<User> findByName(String name);

    @Query("select u from User u where u.uuid = ?1")
    Optional<User> findByUUID(UUID uuid);

    @Query("select u from User u where u.uuid = ?1")
    Optional<User> findByUUID(String uuid);

    @Query("select u from User u where u.username = ?1")
    Optional<User> findByUsername(String username);

    @Modifying
    @Transactional
    @Query(value = "update user_entity set password_user = ?1 where id = ?2", nativeQuery = true)
    void  updatePassword(String password, Long id);

    @Query(value = "select constraint_name from information_schema.constraint_column_usage where " +
            "table_name = 'user_entity_role' and column_name = 'role_id' and constraint_name <>'unique_role_user_entity' " +
            "and constraint_name <> 'uk_role_id'", nativeQuery = true)
    String searchConstraintRole();

    @Query(value = "select constraint_name from information_schema.constraint_column_usage where " +
            "table_name = 'user_entity_role' and column_name = 'role_id' and constraint_name <>'unique_role_user_entity' " +
            "and constraint_name <>'uk_lubu5auf4bujgmat9hwqocejj'", nativeQuery = true)
    String searchConstraintRoleUK();

    default Page<User> findByUsernamePage(String name, PageRequest pageRequest) {
        User user = new User();
        user.setName(name);

        ExampleMatcher matcher = ExampleMatcher.matchingAny()
                .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());

        Example<User> example = Example.of(user, matcher);

        return findAll(example, pageRequest);
    }

//    @Modifying
//    @Query(value = "alter table user_entity_role drop constraint ?1", nativeQuery = true)
//    void removeConstraintRole(String constraint);

//    @Modifying
//    @Query(value = "insert into user_entity_role (user_entity_id, role_id) values(?1, (select id from role where authority = 'ROLE_USER'))", nativeQuery = true)
//    void insertDefaultAccessRole(Long id);
}
