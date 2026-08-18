package com.darmoz.mail.mail;

import com.darmoz.mail.audit.EmailAuditLog;
import com.darmoz.mail.audit.EmailAuditLogRepository;
import com.darmoz.mail.scheduledemail.ScheduledEmail;
import com.darmoz.mail.scheduledemail.ScheduledEmailRepository;
import com.darmoz.mail.scheduledemail.ScheduledEmailStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Component
public class EmailSchedulerJob {

    private final ScheduledEmailRepository scheduledEmailRepository;
    private final EmailAuditLogRepository emailAuditLogRepository;
    private final EmailContentResolver emailContentResolver;
    private final MailSenderService mailSenderService;
    private final int batchSize;
    private final int maxAttempts;

    EmailSchedulerJob(ScheduledEmailRepository scheduledEmailRepository,
                       EmailAuditLogRepository emailAuditLogRepository,
                       EmailContentResolver emailContentResolver,
                       MailSenderService mailSenderService,
                       @Value("${darmoz.mail.scheduler.batch-size}") int batchSize,
                       @Value("${darmoz.mail.scheduler.max-attempts}") int maxAttempts) {
        this.scheduledEmailRepository = scheduledEmailRepository;
        this.emailAuditLogRepository = emailAuditLogRepository;
        this.emailContentResolver = emailContentResolver;
        this.mailSenderService = mailSenderService;
        this.batchSize = batchSize;
        this.maxAttempts = maxAttempts;
    }

    @Scheduled(fixedDelayString = "${darmoz.mail.scheduler.fixed-delay-ms:30000}")
    @Transactional
    public void dispatchPendingEmails() {
        List<ScheduledEmail> due = scheduledEmailRepository.findDueBatch(Instant.now(), PageRequest.of(0, batchSize));

        for (ScheduledEmail scheduledEmail : due) {
            scheduledEmail.setStatus(ScheduledEmailStatus.PROCESSING);
            scheduledEmailRepository.save(scheduledEmail);

            try {
                ResolvedEmail resolved = emailContentResolver.resolve(
                        scheduledEmail.getSubject(),
                        scheduledEmail.getTemplateCode(),
                        scheduledEmail.getVariables(),
                        scheduledEmail.getBodyOverride());

                mailSenderService.send(scheduledEmail.getRecipient(), resolved.subject(), resolved.body());

                Instant sentAt = Instant.now();
                scheduledEmail.setStatus(ScheduledEmailStatus.SENT);
                scheduledEmail.setSentAt(sentAt);

                EmailAuditLog auditLog = new EmailAuditLog();
                auditLog.setRecipient(scheduledEmail.getRecipient());
                auditLog.setSubject(resolved.subject());
                auditLog.setBodyHtml(resolved.body());
                auditLog.setAccion(scheduledEmail.getTemplateCode());
                auditLog.setSentAt(sentAt);
                auditLog.setClientId(scheduledEmail.getClientId());
                auditLog.setScheduledEmailId(scheduledEmail.getId());
                emailAuditLogRepository.save(auditLog);
            } catch (Exception ex) {
                scheduledEmail.setAttempts(scheduledEmail.getAttempts() + 1);
                scheduledEmail.setLastError(ex.getMessage());
                scheduledEmail.setStatus(scheduledEmail.getAttempts() >= maxAttempts
                        ? ScheduledEmailStatus.FAILED
                        : ScheduledEmailStatus.PENDING);
            }

            scheduledEmailRepository.save(scheduledEmail);
        }
    }
}
