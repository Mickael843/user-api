package com.mikaeru.user_api.controller.v1.phone;

import com.mikaeru.user_api.domain.service.phone.PhoneService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/v1/users/{userUUID}/phones")
@Api(produces = APPLICATION_JSON_VALUE, description = "Responsável pelas Operações CRUD de um telefone.", tags = {"Phone Controller"})
public class PhoneController {

    @Autowired private PhoneService phoneService;

    /**
     * Método responsável por deletar um telefone de um usuário especifico.
     *
     * @param userUUID id externo do proprietário do telefone
     * @param phoneUUID id externo do telefone
     * @return <code>{@link ResponseEntity}</code> object
     *
     * HTTP STATUS:
     *
     * 204 (No Content) - Se tudo ocorrer bem, sem erros.
     * 404 (Not Found) - Caso o usuário ou o telefone não seja encontrado.
     * 500 (Internal Server Error) - Erro interno do servidor.
     */
    @DeleteMapping("/{phoneUUID}")
    public ResponseEntity<?> delete(@PathVariable UUID userUUID, @PathVariable UUID phoneUUID) {
        phoneService.delete(userUUID, phoneUUID);
        return ResponseEntity.noContent().build();
    }
}
