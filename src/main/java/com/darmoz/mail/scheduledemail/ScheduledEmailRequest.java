package com.darmoz.mail.scheduledemail;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public record ScheduledEmailRequest(

        @NotBlank(message = "el recipient es obligatorio")
        @Email(message = "el recipient no es un email valido")
        String recipient,

        String subject,

        String templateCode,

        Map<String, String> variables,

        String bodyOverride,

        @NotNull(message = "el scheduledAt es obligatorio")
        Instant scheduledAt,

        @NotNull(message = "el clientId es obligatorio")
        UUID clientId
) {
}
