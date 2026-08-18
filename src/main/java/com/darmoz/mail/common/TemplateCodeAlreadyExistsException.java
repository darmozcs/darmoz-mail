package com.darmoz.mail.common;

public class TemplateCodeAlreadyExistsException extends RuntimeException {

    public TemplateCodeAlreadyExistsException(String code) {
        super("Ya existe un template con codigo " + code);
    }
}
