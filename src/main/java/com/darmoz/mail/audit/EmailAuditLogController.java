package com.darmoz.mail.audit;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/audit-logs")
public class EmailAuditLogController {

    private final EmailAuditLogService emailAuditLogService;

    EmailAuditLogController(EmailAuditLogService emailAuditLogService) {
        this.emailAuditLogService = emailAuditLogService;
    }

    @GetMapping
    List<EmailAuditLogResponse> list(@RequestParam(required = false) String recipient,
                                      @RequestParam(required = false) UUID clientId,
                                      @RequestParam(required = false) String accion,
                                      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
                                      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to) {
        return emailAuditLogService.search(recipient, clientId, accion, from, to);
    }

    @PostMapping("/{id}/resend")
    EmailAuditLogResponse resend(@PathVariable Long id) {
        return emailAuditLogService.resend(id);
    }
}
