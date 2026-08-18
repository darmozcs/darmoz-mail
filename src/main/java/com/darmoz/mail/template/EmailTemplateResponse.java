package com.darmoz.mail.template;

import java.time.Instant;

public record EmailTemplateResponse(
        Long id,
        String code,
        String name,
        String subject,
        String bodyHtml,
        String bodyText,
        boolean active,
        Instant createdAt,
        Instant updatedAt
) {

    public static EmailTemplateResponse from(EmailTemplate template) {
        return new EmailTemplateResponse(
                template.getId(),
                template.getCode(),
                template.getName(),
                template.getSubject(),
                template.getBodyHtml(),
                template.getBodyText(),
                template.isActive(),
                template.getCreatedAt(),
                template.getUpdatedAt()
        );
    }
}
