package com.mikaeru.user_api.domain.service.email;

import com.mikaeru.user_api.domain.handler.Problem;

/**
 * Interface que abstrai o envio de um email de recuperação de senha de um usuário.
 * @author Mickael Luiz
 */
public interface SendEmailService {

    /**
     * Método responsável por enviar um email de recuperação de senha para o usuário com a nova senha gerada.
     *
     * @param username nome de usuário utilizado para realizar o login no sistema
     * @return <code>{@link Problem}</code> object
     */
    Problem sendRecoveryEmail(String username);
}
