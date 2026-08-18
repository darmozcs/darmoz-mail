package com.darmoz.mail.audit;

import com.darmoz.mail.common.AuditLogContentUnavailableException;
import com.darmoz.mail.common.AuditLogNotFoundException;
import com.darmoz.mail.mail.MailSenderService;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
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
        Specification<EmailAuditLog> spec = buildSpecification(recipient, clientId, accion, from, to);

        return emailAuditLogRepository.findAll(spec, Sort.by(Sort.Direction.DESC, "sentAt")).stream()
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

    private Specification<EmailAuditLog> buildSpecification(String recipient, UUID clientId, String accion,
                                                              Instant from, Instant to) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (recipient != null) {
                predicates.add(cb.equal(root.get("recipient"), recipient));
            }
            if (clientId != null) {
                predicates.add(cb.equal(root.get("clientId"), clientId));
            }
            if (accion != null) {
                predicates.add(cb.equal(root.get("accion"), accion));
            }
            if (from != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("sentAt"), from));
            }
            if (to != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("sentAt"), to));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
