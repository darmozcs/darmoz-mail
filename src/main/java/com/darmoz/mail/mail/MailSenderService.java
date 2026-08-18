package com.darmoz.mail.mail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.regex.Pattern;

@Service
public class MailSenderService {

    private static final Pattern HTML_TAG_PATTERN = Pattern.compile("<[^>]+>");

    private final JavaMailSender javaMailSender;
    private final String fromAddress;
    private final String fromName;

    MailSenderService(JavaMailSender javaMailSender,
                       @Value("${darmoz.mail.from-address}") String fromAddress,
                       @Value("${darmoz.mail.from-name}") String fromName) {
        this.javaMailSender = javaMailSender;
        this.fromAddress = fromAddress;
        this.fromName = fromName;
    }

    public void send(String recipient, String subject, String bodyHtml) {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(fromAddress, fromName);
            helper.setTo(recipient);
            helper.setSubject(subject);
            helper.setText(HTML_TAG_PATTERN.matcher(bodyHtml).replaceAll(""), bodyHtml);
            javaMailSender.send(message);
        } catch (MessagingException | UnsupportedEncodingException ex) {
            throw new MailSendException("No se pudo enviar el correo a " + recipient, ex);
        }
    }
}
