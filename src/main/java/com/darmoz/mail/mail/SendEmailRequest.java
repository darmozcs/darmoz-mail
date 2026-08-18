package com.darmoz.mail.mail;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.time.Instant;
import java.util.Map;

public record SendEmailRequest(

        @NotBlank(message = "el to es obligatorio")
        @Email(message = "el to no es un email valido")
        String to,

        String subject,

        String accion,

        Map<String, String> variables,

        String bodyHtml,

        Instant scheduledAt
) {
}
