package com.mikaeru.user_api.controller.v1.phone;

import com.mikaeru.user_api.domain.model.user.User;
import com.mikaeru.user_api.domain.service.phone.CrudPhoneService;
import com.mikaeru.user_api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(value = "/v1/users/{uuid}/phones", produces = "application/json")
public class phoneController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CrudPhoneService phoneService;

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePhone(@PathVariable UUID uuid, @PathVariable Long id) {
        Optional<User> userOptional = userRepository.findByUUID(uuid);

        if (userOptional.isPresent()) {
            phoneService.deletePhoneById(id);
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }
}
