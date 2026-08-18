package com.darmoz.mail.common;

public class MissingTemplateVariableException extends RuntimeException {

    public MissingTemplateVariableException(String variableName) {
        super("Falta un valor para la variable de template " + variableName);
    }
}
