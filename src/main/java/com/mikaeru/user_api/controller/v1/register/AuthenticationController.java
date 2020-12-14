package com.mikaeru.user_api.controller.v1.register;

import com.mikaeru.user_api.domain.model.user.User;
import com.mikaeru.user_api.domain.service.user.UserService;
import com.mikaeru.user_api.dto.user.in.UserInput;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@CrossOrigin(origins = "*")
@Api(produces = APPLICATION_JSON_VALUE, description = "Responsável por registrar um novo usuário.", tags = {"Register"})
public class AuthenticationController {

    @Autowired private UserService userService;

    @PostMapping(value = "/register", produces = APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Method responsible for registering new users in the system.", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserInput userIn) {

        User user = userService.save(userIn.convertToEntity());

        return ResponseEntity.created(
                ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/v1/users/{externalId}")
                        .buildAndExpand(user.getExternalId())
                        .toUri()
        ).build();
    }
}
