package com.mikaeru.user_api.domain.service.phone;

import com.mikaeru.user_api.domain.model.phone.Phone;

import java.util.List;
import java.util.UUID;

/**
 * Classe que abstrai as operações de (CRUD) de um Telefone.
 * @author Mickael Luiz
 */
public interface PhoneService {

    /**
     * Método responsável por salvar uma lista de telefones.
     *
     * @param phones {@link List<Phone>} lista de telefones
     * @return <code>{@link List<Phone>}</code> lista de telefones salvos
     */
    List<Phone> saveAll(List<Phone> phones);

    /**
     * Responsável por remover um telefone do usuário especificado pelo id externo.
     *
     * @param userUUID id externo do usuário.
     * @param phoneUUID id externo do telefone.
     */
    void delete(UUID userUUID, UUID phoneUUID);
}
