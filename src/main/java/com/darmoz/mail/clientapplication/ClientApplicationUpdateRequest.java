package com.darmoz.mail.clientapplication;

/** Update parcial: los campos ausentes (null) no se modifican. */
public record ClientApplicationUpdateRequest(

        String name,

        Boolean active
) {
}
