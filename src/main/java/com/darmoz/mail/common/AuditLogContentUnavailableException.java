package com.darmoz.mail.common;

public class AuditLogContentUnavailableException extends RuntimeException {

    public AuditLogContentUnavailableException(Long id) {
        super("El registro de auditoria " + id + " no tiene contenido guardado para reenviar");
    }
}
