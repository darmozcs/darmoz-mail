package com.darmoz.mail.common;

public class AuditLogNotFoundException extends RuntimeException {

    public AuditLogNotFoundException(Long id) {
        super("No existe un registro de auditoria con id " + id);
    }
}
