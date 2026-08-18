package com.darmoz.mail.common;

public class UnknownClientException extends RuntimeException {

    public UnknownClientException(String clientId) {
        super("El clientId '" + clientId + "' no esta registrado o no esta activo en aplicaciones");
    }
}
