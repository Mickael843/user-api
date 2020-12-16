package com.mikaeru.user_api.domain.validation.user;

/**
 * Interface de validação dos campos de usuário.
 * @author Mickael Luiz
 */
public interface UserValidationGroup {

    interface CreateValidation { }
    interface UpdateValidation { }
    interface RecoveryValidation { }
}
