package com.darmoz.mail.common;

public class ScheduledEmailNotFoundException extends RuntimeException {

    public ScheduledEmailNotFoundException(Long id) {
        super("No existe un correo agendado con id " + id);
    }
}
