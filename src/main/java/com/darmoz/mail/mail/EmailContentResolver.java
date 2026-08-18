package com.darmoz.mail.mail;

import com.darmoz.mail.common.TemplateNotFoundException;
import com.darmoz.mail.template.EmailTemplate;
import com.darmoz.mail.template.EmailTemplateRepository;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class EmailContentResolver {

    private final EmailTemplateRepository emailTemplateRepository;
    private final TemplateRenderer templateRenderer;

    EmailContentResolver(EmailTemplateRepository emailTemplateRepository, TemplateRenderer templateRenderer) {
        this.emailTemplateRepository = emailTemplateRepository;
        this.templateRenderer = templateRenderer;
    }

    public ResolvedEmail resolve(String subjectOverride, String accion, Map<String, String> variables, String bodyOverride) {
        String subject;
        String body;

        if (accion != null && !accion.isBlank()) {
            String code = accion.toUpperCase();
            EmailTemplate template = emailTemplateRepository.findByCodeAndActiveTrue(code)
                    .orElseThrow(() -> new TemplateNotFoundException(code));

            subject = templateRenderer.render(
                    subjectOverride != null && !subjectOverride.isBlank() ? subjectOverride : template.getSubject(),
                    variables);
            body = templateRenderer.render(template.getBodyHtml(), variables);
        } else {
            subject = templateRenderer.render(subjectOverride, variables);
            body = templateRenderer.render(bodyOverride, variables);
        }

        return new ResolvedEmail(subject == null ? "" : subject, body);
    }
}
