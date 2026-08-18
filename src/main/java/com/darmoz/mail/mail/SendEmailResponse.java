package com.darmoz.mail.mail;

import java.time.Instant;

public record SendEmailResponse(
        String status,
        Long scheduledEmailId,
        Instant sentAt
) {

    public static SendEmailResponse sent(Instant sentAt) {
        return new SendEmailResponse("SENT", null, sentAt);
    }

    public static SendEmailResponse scheduled(Long scheduledEmailId) {
        return new SendEmailResponse("SCHEDULED", scheduledEmailId, null);
    }
}
