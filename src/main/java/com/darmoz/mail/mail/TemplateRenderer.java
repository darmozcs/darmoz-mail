package com.darmoz.mail.mail;

import com.darmoz.mail.common.MissingTemplateVariableException;
import org.apache.commons.text.StringSubstitutor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class TemplateRenderer {

    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("\\$\\{([^}]+)}");

    public String render(String content, Map<String, String> variables) {
        if (content == null) {
            return null;
        }

        Map<String, String> safeVariables = variables == null ? Map.of() : variables;
        validarPlaceholders(content, safeVariables);

        return new StringSubstitutor(safeVariables).replace(content);
    }

    private void validarPlaceholders(String content, Map<String, String> variables) {
        Matcher matcher = PLACEHOLDER_PATTERN.matcher(content);
        while (matcher.find()) {
            String variableName = matcher.group(1);
            if (!variables.containsKey(variableName)) {
                throw new MissingTemplateVariableException(variableName);
            }
        }
    }
}
