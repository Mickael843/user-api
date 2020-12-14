package com.mikaeru.user_api.domain.exception;

public class DuplicatedDataException extends DomainException {

    public DuplicatedDataException(Error invalidDuplicatedData) {
        super(invalidDuplicatedData);
    }
}
