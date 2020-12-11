package com.mikaeru.user_api.domain.service.profession.serviceImpl;

import com.mikaeru.user_api.domain.model.user.profession.Profession;
import com.mikaeru.user_api.domain.service.profession.CrudProfessionService;
import com.mikaeru.user_api.repository.ProfessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CrudProfessionServiceImpl implements CrudProfessionService {

    @Autowired
    private ProfessionRepository professionRepository;

    @Override
    public Profession saveProfession(Profession profession) {
        return professionRepository.save(profession);
    }

    @Override
    public void deleteById(Long id) {
        professionRepository.deleteById(id);
    }
}
