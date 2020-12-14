package com.mikaeru.user_api.controller.v1.user;

import com.mikaeru.user_api.domain.model.user.User;
import com.mikaeru.user_api.domain.service.email.SendEmailService;
import com.mikaeru.user_api.domain.validation.user.UserValidationGroup.RecoveryValidation;
import com.mikaeru.user_api.domain.handler.Problem;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/v1/recovery", produces = "application/json")
@Api(produces = APPLICATION_JSON_VALUE, description = "Responsável pela recuperação da senha do usuário.", tags = {"Recovery"})
public class RecoveryController {

    @Autowired private SendEmailService emailService;

    @PostMapping
    public ResponseEntity<Problem> recovery(@Validated(RecoveryValidation.class) @RequestBody User user) {
        return ResponseEntity.ok(emailService.sendRecoveryEmail(user.getUsername()));
    }
}
