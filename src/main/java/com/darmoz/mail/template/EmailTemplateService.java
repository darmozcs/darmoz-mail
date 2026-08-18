package com.darmoz.mail.template;

import com.darmoz.mail.common.TemplateCodeAlreadyExistsException;
import com.darmoz.mail.common.TemplateNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EmailTemplateService {

    private final EmailTemplateRepository emailTemplateRepository;

    EmailTemplateService(EmailTemplateRepository emailTemplateRepository) {
        this.emailTemplateRepository = emailTemplateRepository;
    }

    @Transactional
    public EmailTemplateResponse create(EmailTemplateRequest request) {
        String code = request.code().toUpperCase();
        if (emailTemplateRepository.existsByCode(code)) {
            throw new TemplateCodeAlreadyExistsException(code);
        }

        EmailTemplate template = new EmailTemplate();
        aplicarDatos(template, request, code);

        return EmailTemplateResponse.from(emailTemplateRepository.save(template));
    }

    @Transactional
    public EmailTemplateResponse update(Long id, EmailTemplateRequest request) {
        EmailTemplate template = emailTemplateRepository.findById(id)
                .orElseThrow(() -> new TemplateNotFoundException(id));

        String code = request.code().toUpperCase();
        if (emailTemplateRepository.existsByCodeAndIdNot(code, id)) {
            throw new TemplateCodeAlreadyExistsException(code);
        }

        aplicarDatos(template, request, code);

        return EmailTemplateResponse.from(emailTemplateRepository.save(template));
    }

    @Transactional(readOnly = true)
    public EmailTemplateResponse getById(Long id) {
        return emailTemplateRepository.findById(id)
                .map(EmailTemplateResponse::from)
                .orElseThrow(() -> new TemplateNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public List<EmailTemplateResponse> list(Boolean active) {
        List<EmailTemplate> templates = active == null
                ? emailTemplateRepository.findAll()
                : emailTemplateRepository.findByActive(active);

        return templates.stream().map(EmailTemplateResponse::from).toList();
    }

    @Transactional
    public void delete(Long id) {
        if (!emailTemplateRepository.existsById(id)) {
            throw new TemplateNotFoundException(id);
        }
        emailTemplateRepository.deleteById(id);
    }

    private void aplicarDatos(EmailTemplate template, EmailTemplateRequest request, String code) {
        template.setCode(code);
        template.setName(request.name());
        template.setSubject(request.subject());
        template.setBodyHtml(request.bodyHtml());
        template.setBodyText(request.bodyText());
        template.setActive(request.active());
    }
}
