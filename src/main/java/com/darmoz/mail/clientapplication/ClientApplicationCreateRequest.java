package com.darmoz.mail.clientapplication;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ClientApplicationCreateRequest(

        @NotBlank(message = "el name es obligatorio")
        @Size(max = 150, message = "el name no puede superar 150 caracteres")
        String name,

        Boolean active
) {
}
