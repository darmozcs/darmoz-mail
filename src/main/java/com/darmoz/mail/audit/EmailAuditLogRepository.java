package com.darmoz.mail.audit;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface EmailAuditLogRepository extends JpaRepository<EmailAuditLog, Long>, JpaSpecificationExecutor<EmailAuditLog> {
}
