package com.darmoz.mail.mail;

import com.darmoz.mail.audit.EmailAuditLog;
import com.darmoz.mail.audit.EmailAuditLogRepository;
import com.darmoz.mail.clientapplication.ClientApplicationRepository;
import com.darmoz.mail.common.MissingEmailContentException;
import com.darmoz.mail.common.UnknownClientException;
import com.darmoz.mail.scheduledemail.ScheduledEmail;
import com.darmoz.mail.scheduledemail.ScheduledEmailRepository;
import com.darmoz.mail.scheduledemail.ScheduledEmailStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
public class SendEmailService {

    private final EmailContentResolver emailContentResolver;
    private final MailSenderService mailSenderService;
    private final ScheduledEmailRepository scheduledEmailRepository;
    private final EmailAuditLogRepository emailAuditLogRepository;
    private final ClientApplicationRepository clientApplicationRepository;

    SendEmailService(EmailContentResolver emailContentResolver,
                      MailSenderService mailSenderService,
                      ScheduledEmailRepository scheduledEmailRepository,
                      EmailAuditLogRepository emailAuditLogRepository,
                      ClientApplicationRepository clientApplicationRepository) {
        this.emailContentResolver = emailContentResolver;
        this.mailSenderService = mailSenderService;
        this.scheduledEmailRepository = scheduledEmailRepository;
        this.emailAuditLogRepository = emailAuditLogRepository;
        this.clientApplicationRepository = clientApplicationRepository;
    }

    @Transactional
    public SendEmailResponse send(SendEmailRequest request, String clientIdHeader) {
        UUID clientId = parseAndValidateClientId(clientIdHeader);

        boolean sinAccion = request.accion() == null || request.accion().isBlank();
        boolean sinBodyHtml = request.bodyHtml() == null || request.bodyHtml().isBlank();
        if (sinAccion && sinBodyHtml) {
            throw new MissingEmailContentException();
        }

        if (request.scheduledAt() == null || !request.scheduledAt().isAfter(Instant.now())) {
            return sendNow(request, clientId);
        }

        return schedule(request, clientId);
    }

    private UUID parseAndValidateClientId(String clientIdHeader) {
        UUID clientId;
        try {
            clientId = UUID.fromString(clientIdHeader);
        } catch (IllegalArgumentException ex) {
            throw new UnknownClientException(clientIdHeader);
        }

        if (!clientApplicationRepository.existsByIdAndActiveTrue(clientId)) {
            throw new UnknownClientException(clientIdHeader);
        }

        return clientId;
    }

    private SendEmailResponse sendNow(SendEmailRequest request, UUID clientId) {
        ResolvedEmail resolved = emailContentResolver.resolve(
                request.subject(), request.accion(), request.variables(), request.bodyHtml());

        mailSenderService.send(request.to(), resolved.subject(), resolved.body());

        Instant sentAt = Instant.now();

        EmailAuditLog auditLog = new EmailAuditLog();
        auditLog.setRecipient(request.to());
        auditLog.setSubject(resolved.subject());
        auditLog.setBodyHtml(resolved.body());
        auditLog.setAccion(request.accion() == null || request.accion().isBlank() ? null : request.accion().toUpperCase());
        auditLog.setSentAt(sentAt);
        auditLog.setClientId(clientId);
        emailAuditLogRepository.save(auditLog);

        return SendEmailResponse.sent(sentAt);
    }

    private SendEmailResponse schedule(SendEmailRequest request, UUID clientId) {
        ScheduledEmail scheduledEmail = new ScheduledEmail();
        scheduledEmail.setRecipient(request.to());
        scheduledEmail.setSubject(request.subject());
        scheduledEmail.setTemplateCode(request.accion() == null ? null : request.accion().toUpperCase());
        scheduledEmail.setVariables(request.variables());
        scheduledEmail.setBodyOverride(request.bodyHtml());
        scheduledEmail.setScheduledAt(request.scheduledAt());
        scheduledEmail.setStatus(ScheduledEmailStatus.PENDING);
        scheduledEmail.setClientId(clientId);

        scheduledEmail = scheduledEmailRepository.save(scheduledEmail);

        return SendEmailResponse.scheduled(scheduledEmail.getId());
    }
}
