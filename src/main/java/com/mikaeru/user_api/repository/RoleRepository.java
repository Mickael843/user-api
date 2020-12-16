package com.mikaeru.user_api.repository;

import com.mikaeru.user_api.domain.model.role.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

/**
 * Interface que implementa as operações CRUD da classe Role.
 * @author Mickael Luiz
 */
@Repository
@Transactional
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByAuthority(String authority);
}
