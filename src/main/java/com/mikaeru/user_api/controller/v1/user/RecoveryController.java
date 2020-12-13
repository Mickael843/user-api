package com.mikaeru.user_api.controller.v1.user;

import com.mikaeru.user_api.domain.model.user.User;
import com.mikaeru.user_api.domain.service.email.SendEmailService;
import com.mikaeru.user_api.domain.validation.user.UserValidationGroup.RecoveryValidation;
import com.mikaeru.user_api.exceptionHandler.Problem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/v1/recovery", produces = "application/json")
public class RecoveryController {

    @Autowired private SendEmailService emailService;

    @PostMapping
    public ResponseEntity<Problem> recovery(@Validated(RecoveryValidation.class) @RequestBody User user) {
        return ResponseEntity.ok(emailService.sendRecoveryEmail(user.getUsername()));
    }
}
