package com.darmoz.mail.mail;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/emails")
public class SendEmailController {

    private final SendEmailService sendEmailService;

    SendEmailController(SendEmailService sendEmailService) {
        this.sendEmailService = sendEmailService;
    }

    @PostMapping
    ResponseEntity<SendEmailResponse> send(@Valid @RequestBody SendEmailRequest request,
                                            @RequestHeader("X-Client-Id") String clientId) {
        SendEmailResponse response = sendEmailService.send(request, clientId);
        HttpStatus status = "SCHEDULED".equals(response.status()) ? HttpStatus.ACCEPTED : HttpStatus.OK;
        return ResponseEntity.status(status).body(response);
    }
}
