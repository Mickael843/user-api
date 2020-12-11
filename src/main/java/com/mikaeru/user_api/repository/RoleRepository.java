package com.mikaeru.user_api.repository;

import com.mikaeru.user_api.domain.model.role.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.Entity;
import javax.transaction.Transactional;
import java.util.Optional;

@Entity
@Transactional
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByAuthority(String authority);
}
