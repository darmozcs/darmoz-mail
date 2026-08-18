package com.darmoz.mail.scheduledemail;

import com.darmoz.mail.clientapplication.ClientApplicationRepository;
import com.darmoz.mail.common.MissingEmailContentException;
import com.darmoz.mail.common.ScheduledEmailNotEditableException;
import com.darmoz.mail.common.ScheduledEmailNotFoundException;
import com.darmoz.mail.common.UnknownClientException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class ScheduledEmailService {

    private final ScheduledEmailRepository scheduledEmailRepository;
    private final ClientApplicationRepository clientApplicationRepository;

    ScheduledEmailService(ScheduledEmailRepository scheduledEmailRepository,
                           ClientApplicationRepository clientApplicationRepository) {
        this.scheduledEmailRepository = scheduledEmailRepository;
        this.clientApplicationRepository = clientApplicationRepository;
    }

    @Transactional
    public ScheduledEmailResponse create(ScheduledEmailRequest request) {
        validarContenido(request.templateCode(), request.bodyOverride());
        validarCliente(request.clientId());

        ScheduledEmail scheduledEmail = new ScheduledEmail();
        aplicarDatos(scheduledEmail, request);
        scheduledEmail.setStatus(ScheduledEmailStatus.PENDING);

        return ScheduledEmailResponse.from(scheduledEmailRepository.save(scheduledEmail));
    }

    @Transactional
    public ScheduledEmailResponse update(Long id, ScheduledEmailRequest request) {
        validarContenido(request.templateCode(), request.bodyOverride());
        validarCliente(request.clientId());

        ScheduledEmail scheduledEmail = scheduledEmailRepository.findById(id)
                .orElseThrow(() -> new ScheduledEmailNotFoundException(id));

        if (scheduledEmail.getStatus() != ScheduledEmailStatus.PENDING) {
            throw new ScheduledEmailNotEditableException(id, scheduledEmail.getStatus().name());
        }

        aplicarDatos(scheduledEmail, request);

        return ScheduledEmailResponse.from(scheduledEmailRepository.save(scheduledEmail));
    }

    @Transactional(readOnly = true)
    public ScheduledEmailResponse getById(Long id) {
        return scheduledEmailRepository.findById(id)
                .map(ScheduledEmailResponse::from)
                .orElseThrow(() -> new ScheduledEmailNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public List<ScheduledEmailResponse> list(ScheduledEmailStatus status) {
        List<ScheduledEmail> scheduledEmails = status == null
                ? scheduledEmailRepository.findAll()
                : scheduledEmailRepository.findByStatus(status);

        return scheduledEmails.stream().map(ScheduledEmailResponse::from).toList();
    }

    @Transactional
    public void cancel(Long id) {
        ScheduledEmail scheduledEmail = scheduledEmailRepository.findById(id)
                .orElseThrow(() -> new ScheduledEmailNotFoundException(id));

        if (scheduledEmail.getStatus() == ScheduledEmailStatus.SENT) {
            throw new ScheduledEmailNotEditableException(id, scheduledEmail.getStatus().name());
        }

        scheduledEmail.setStatus(ScheduledEmailStatus.CANCELLED);
        scheduledEmailRepository.save(scheduledEmail);
    }

    private void validarContenido(String templateCode, String bodyOverride) {
        boolean sinTemplate = templateCode == null || templateCode.isBlank();
        boolean sinBody = bodyOverride == null || bodyOverride.isBlank();
        if (sinTemplate && sinBody) {
            throw new MissingEmailContentException();
        }
    }

    private void validarCliente(UUID clientId) {
        if (!clientApplicationRepository.existsByIdAndActiveTrue(clientId)) {
            throw new UnknownClientException(String.valueOf(clientId));
        }
    }

    private void aplicarDatos(ScheduledEmail scheduledEmail, ScheduledEmailRequest request) {
        scheduledEmail.setRecipient(request.recipient());
        scheduledEmail.setSubject(request.subject());
        scheduledEmail.setTemplateCode(request.templateCode() == null ? null : request.templateCode().toUpperCase());
        scheduledEmail.setVariables(request.variables());
        scheduledEmail.setBodyOverride(request.bodyOverride());
        scheduledEmail.setScheduledAt(request.scheduledAt());
        scheduledEmail.setClientId(request.clientId());
    }
}
