package com.darmoz.mail.audit;

import com.darmoz.mail.common.AuditLogContentUnavailableException;
import com.darmoz.mail.common.AuditLogNotFoundException;
import com.darmoz.mail.mail.MailSenderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class EmailAuditLogService {

    private final EmailAuditLogRepository emailAuditLogRepository;
    private final MailSenderService mailSenderService;

    EmailAuditLogService(EmailAuditLogRepository emailAuditLogRepository, MailSenderService mailSenderService) {
        this.emailAuditLogRepository = emailAuditLogRepository;
        this.mailSenderService = mailSenderService;
    }

    @Transactional(readOnly = true)
    public List<EmailAuditLogResponse> search(String recipient, UUID clientId, String accion, Instant from, Instant to) {
        return emailAuditLogRepository.search(recipient, clientId, accion, from, to).stream()
                .map(EmailAuditLogResponse::from)
                .toList();
    }

    @Transactional
    public EmailAuditLogResponse resend(Long id) {
        EmailAuditLog original = emailAuditLogRepository.findById(id)
                .orElseThrow(() -> new AuditLogNotFoundException(id));

        if (original.getBodyHtml() == null) {
            throw new AuditLogContentUnavailableException(id);
        }

        mailSenderService.send(original.getRecipient(), original.getSubject(), original.getBodyHtml());

        EmailAuditLog resent = new EmailAuditLog();
        resent.setRecipient(original.getRecipient());
        resent.setSubject(original.getSubject());
        resent.setBodyHtml(original.getBodyHtml());
        resent.setAccion(original.getAccion());
        resent.setSentAt(Instant.now());
        resent.setClientId(original.getClientId());
        resent.setScheduledEmailId(null);

        return EmailAuditLogResponse.from(emailAuditLogRepository.save(resent));
    }
}
