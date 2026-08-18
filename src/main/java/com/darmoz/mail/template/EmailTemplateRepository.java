package com.darmoz.mail.template;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmailTemplateRepository extends JpaRepository<EmailTemplate, Long> {

    Optional<EmailTemplate> findByCodeAndActiveTrue(String code);

    List<EmailTemplate> findByActive(boolean active);

    boolean existsByCode(String code);

    boolean existsByCodeAndIdNot(String code, Long id);
}
