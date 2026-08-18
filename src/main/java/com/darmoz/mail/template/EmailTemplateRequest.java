package com.darmoz.mail.template;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record EmailTemplateRequest(

        @NotBlank(message = "el code es obligatorio")
        @Size(max = 100, message = "el code no puede superar 100 caracteres")
        String code,

        @NotBlank(message = "el name es obligatorio")
        @Size(max = 150, message = "el name no puede superar 150 caracteres")
        String name,

        @NotBlank(message = "el subject es obligatorio")
        String subject,

        @NotBlank(message = "el bodyHtml es obligatorio")
        String bodyHtml,

        String bodyText,

        @NotNull(message = "active es obligatorio")
        Boolean active
) {
}
