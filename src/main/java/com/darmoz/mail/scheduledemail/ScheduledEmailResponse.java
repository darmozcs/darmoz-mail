package com.darmoz.mail.scheduledemail;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public record ScheduledEmailResponse(
        Long id,
        String recipient,
        String subject,
        String templateCode,
        Map<String, String> variables,
        String bodyOverride,
        Instant scheduledAt,
        ScheduledEmailStatus status,
        int attempts,
        String lastError,
        Instant sentAt,
        UUID clientId,
        Instant createdAt,
        Instant updatedAt
) {

    public static ScheduledEmailResponse from(ScheduledEmail scheduledEmail) {
        return new ScheduledEmailResponse(
                scheduledEmail.getId(),
                scheduledEmail.getRecipient(),
                scheduledEmail.getSubject(),
                scheduledEmail.getTemplateCode(),
                scheduledEmail.getVariables(),
                scheduledEmail.getBodyOverride(),
                scheduledEmail.getScheduledAt(),
                scheduledEmail.getStatus(),
                scheduledEmail.getAttempts(),
                scheduledEmail.getLastError(),
                scheduledEmail.getSentAt(),
                scheduledEmail.getClientId(),
                scheduledEmail.getCreatedAt(),
                scheduledEmail.getUpdatedAt()
        );
    }
}
