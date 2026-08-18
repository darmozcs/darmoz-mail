package com.darmoz.mail.common;

import java.util.UUID;

public class ClientApplicationNotFoundException extends RuntimeException {

    public ClientApplicationNotFoundException(UUID id) {
        super("No existe una aplicacion con id " + id);
    }
}
