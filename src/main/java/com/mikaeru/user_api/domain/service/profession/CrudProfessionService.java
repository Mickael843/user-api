package com.mikaeru.user_api.domain.service.profession;

import com.mikaeru.user_api.domain.model.user.profession.Profession;

public interface CrudProfessionService {

    Profession saveProfession(Profession profession);

    void deleteById(Long id);
}
