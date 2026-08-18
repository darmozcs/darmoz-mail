package com.darmoz.mail.common;

public class ScheduledEmailNotEditableException extends RuntimeException {

    public ScheduledEmailNotEditableException(Long id, String status) {
        super("El correo agendado " + id + " no se puede modificar, su estado actual es " + status);
    }
}
