package com.mikaeru.user_api.repository;

import com.mikaeru.user_api.domain.model.user.profession.Profession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface ProfessionRepository extends JpaRepository<Profession, Long> {
}
