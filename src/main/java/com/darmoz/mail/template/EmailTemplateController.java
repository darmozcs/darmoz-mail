package com.darmoz.mail.template;

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
@RequestMapping("/templates")
public class EmailTemplateController {

    private final EmailTemplateService emailTemplateService;

    EmailTemplateController(EmailTemplateService emailTemplateService) {
        this.emailTemplateService = emailTemplateService;
    }

    @PostMapping
    ResponseEntity<EmailTemplateResponse> create(@Valid @RequestBody EmailTemplateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(emailTemplateService.create(request));
    }

    @PutMapping("/{id}")
    EmailTemplateResponse update(@PathVariable Long id, @Valid @RequestBody EmailTemplateRequest request) {
        return emailTemplateService.update(id, request);
    }

    @GetMapping("/{id}")
    EmailTemplateResponse get(@PathVariable Long id) {
        return emailTemplateService.getById(id);
    }

    @GetMapping
    List<EmailTemplateResponse> list(@RequestParam(required = false) Boolean active) {
        return emailTemplateService.list(active);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable Long id) {
        emailTemplateService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
