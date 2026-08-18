package com.darmoz.mail.common;

public class MissingEmailContentException extends RuntimeException {

    public MissingEmailContentException() {
        super("Debe indicarse 'accion' o 'bodyHtml'");
    }
}
