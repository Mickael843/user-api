package com.mikaeru.user_api.repository;

import com.mikaeru.user_api.domain.model.phone.Phone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

/**
 * Interface que implementa as operações CRUD de um Telefone.
 * @author Mickael Luiz
 */
@Repository
@Transactional
public interface PhoneRepository extends JpaRepository<Phone, Long> {
    Optional<Phone> findByExternalId(UUID phoneUUID);
}
