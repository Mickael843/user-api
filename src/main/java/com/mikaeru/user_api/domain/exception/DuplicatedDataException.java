package com.mikaeru.user_api.domain.exception;

/**
 * Classe que abstrai a exceção DuplicatedDataException e repassa como erro de dominio
 *
 * @author Mickael Luiz
 */
public class DuplicatedDataException extends DomainException {

    public DuplicatedDataException(Error invalidDuplicatedData) {
        super(invalidDuplicatedData);
    }
}
