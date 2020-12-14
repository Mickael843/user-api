package com.mikaeru.user_api.domain.exception;

import java.util.Arrays;
import java.util.List;

public class DomainException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final int code;
    private final List<String> fields;

    public DomainException(Error error) {
        super(error.message);
        code = error.code;
        fields = error.fields;
    }

    public enum Error {
        INVALID_DUPLICATED_DATA("Dados duplicados!", 1000);

        private final int code;
        private final String message;
        private List<String> fields;

        Error(String message, int code) {
            this.message = message;
            this.code = code;
        }

        public void setFields(List<String> fields) {
            this.fields = fields;
        }

        public static List<String> convertToFieldList(String fields) {
            return Arrays.asList(fields.split("\\|"));
        }
    }

    public int getCode() {
        return code;
    }

    public List<String> getFields() {
        return fields;
    }
}
