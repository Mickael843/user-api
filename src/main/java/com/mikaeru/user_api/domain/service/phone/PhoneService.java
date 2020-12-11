package com.mikaeru.user_api.domain.service.phone;

import java.util.UUID;

public interface PhoneService {

    void delete(UUID userUUID, UUID phoneUUID);
}
