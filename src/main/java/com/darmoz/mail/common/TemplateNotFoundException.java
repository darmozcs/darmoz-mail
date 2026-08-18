package com.darmoz.mail.common;

public class TemplateNotFoundException extends RuntimeException {

    public TemplateNotFoundException(String code) {
        super("No existe un template activo con codigo " + code);
    }

    public TemplateNotFoundException(Long id) {
        super("No existe un template con id " + id);
    }
}
