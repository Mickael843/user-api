package com.mikaeru.user_api.controller.v1.phone;

import com.mikaeru.user_api.domain.service.phone.PhoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping(value = "/v1/users/{userUUID}/phones")
public class PhoneController {

    @Autowired private PhoneService phoneService;

    @DeleteMapping("/{phoneUUID}")
    public ResponseEntity<?> delete(@PathVariable UUID userUUID, @PathVariable UUID phoneUUID) {
        phoneService.delete(userUUID, phoneUUID);
        return ResponseEntity.noContent().build();
    }
}
