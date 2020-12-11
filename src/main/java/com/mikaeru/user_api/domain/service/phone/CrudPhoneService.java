package com.mikaeru.user_api.domain.service.phone;

import com.mikaeru.user_api.domain.model.phone.Phone;

import java.util.List;

public interface CrudPhoneService {

    Phone savePhone(Phone phone);

    void deletePhoneById(Long id);

    List<Phone> saveAllPhone(List<Phone> phones);
}
