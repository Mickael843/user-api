package com.mikaeru.user_api.controller.v1.user;

import com.mikaeru.user_api.domain.handler.Problem;
import com.mikaeru.user_api.domain.service.email.SendEmailService;
import com.mikaeru.user_api.domain.validation.user.UserValidationGroup.RecoveryValidation;
import com.mikaeru.user_api.dto.user.in.UserInput;
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
@RequestMapping(value = "/recovery", produces = "application/json")
@Api(produces = APPLICATION_JSON_VALUE, description = "Responsável pela recuperação da senha do usuário.", tags = {"Recovery"})
public class RecoveryController {

    @Autowired private SendEmailService emailService;

    /**
     * Método responsável pela recuperação da senho do usuário.
     *
     * @param userIn  {@link UserInput} Objeto de transferência de dados.
     * @return <code>{@link ResponseEntity}</code> object
     *
     * HTTP STATUS:
     *
     * 200 (OK) - Fluxo de envio de email e recuperação de senha.
     * 400 (Bad Request) - Dados enviados são inválidos.
     * 404 (Not Found) - Usuário não encontrado.
     * 500 (Internal Server Error) - Erro do servidor.
     */
    @PostMapping
    public ResponseEntity<Problem> recovery(@Validated(RecoveryValidation.class) @RequestBody UserInput userIn) {
        return ResponseEntity.ok(emailService.sendRecoveryEmail(userIn.getUsername()));
    }
}
