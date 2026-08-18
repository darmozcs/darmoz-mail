package com.darmoz.mail.audit;

import java.time.Instant;
import java.util.UUID;

public record EmailAuditLogResponse(
        Long id,
        String recipient,
        String subject,
        Instant sentAt,
        UUID clientId,
        Long scheduledEmailId,
        String accion,
        boolean resendable,
        Instant createdAt
) {

    public static EmailAuditLogResponse from(EmailAuditLog auditLog) {
        return new EmailAuditLogResponse(
                auditLog.getId(),
                auditLog.getRecipient(),
                auditLog.getSubject(),
                auditLog.getSentAt(),
                auditLog.getClientId(),
                auditLog.getScheduledEmailId(),
                auditLog.getAccion(),
                auditLog.getBodyHtml() != null,
                auditLog.getCreatedAt()
        );
    }
}
