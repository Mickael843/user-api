package com.mikaeru.user_api.domain.service.phone;

import com.mikaeru.user_api.domain.model.phone.Phone;

import java.util.List;
import java.util.UUID;

public interface PhoneService {

    List<Phone> saveAll(List<Phone> phones);

    void delete(UUID userUUID, UUID phoneUUID);
}
