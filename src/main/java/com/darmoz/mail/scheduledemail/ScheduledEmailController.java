package com.darmoz.mail.scheduledemail;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/scheduled-emails")
public class ScheduledEmailController {

    private final ScheduledEmailService scheduledEmailService;

    ScheduledEmailController(ScheduledEmailService scheduledEmailService) {
        this.scheduledEmailService = scheduledEmailService;
    }

    @PostMapping
    ResponseEntity<ScheduledEmailResponse> create(@Valid @RequestBody ScheduledEmailRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(scheduledEmailService.create(request));
    }

    @PutMapping("/{id}")
    ScheduledEmailResponse update(@PathVariable Long id, @Valid @RequestBody ScheduledEmailRequest request) {
        return scheduledEmailService.update(id, request);
    }

    @GetMapping("/{id}")
    ScheduledEmailResponse get(@PathVariable Long id) {
        return scheduledEmailService.getById(id);
    }

    @GetMapping
    List<ScheduledEmailResponse> list(@RequestParam(required = false) ScheduledEmailStatus status) {
        return scheduledEmailService.list(status);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> cancel(@PathVariable Long id) {
        scheduledEmailService.cancel(id);
        return ResponseEntity.noContent().build();
    }
}
