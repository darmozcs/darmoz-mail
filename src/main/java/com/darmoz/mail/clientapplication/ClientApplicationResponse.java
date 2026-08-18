package com.darmoz.mail.clientapplication;

import java.time.Instant;
import java.util.UUID;

public record ClientApplicationResponse(
        UUID id,
        String name,
        boolean active,
        Instant createdAt,
        Instant updatedAt
) {

    public static ClientApplicationResponse from(ClientApplication clientApplication) {
        return new ClientApplicationResponse(
                clientApplication.getId(),
                clientApplication.getName(),
                clientApplication.isActive(),
                clientApplication.getCreatedAt(),
                clientApplication.getUpdatedAt()
        );
    }
}
