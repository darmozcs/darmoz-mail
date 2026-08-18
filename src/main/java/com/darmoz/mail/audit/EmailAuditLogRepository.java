package com.darmoz.mail.audit;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface EmailAuditLogRepository extends JpaRepository<EmailAuditLog, Long> {

    @Query("select a from EmailAuditLog a "
            + "where (:recipient is null or a.recipient = :recipient) "
            + "and (:clientId is null or a.clientId = :clientId) "
            + "and (:accion is null or a.accion = :accion) "
            + "and (:from is null or a.sentAt >= :from) "
            + "and (:to is null or a.sentAt <= :to) "
            + "order by a.sentAt desc")
    List<EmailAuditLog> search(@Param("recipient") String recipient,
                                @Param("clientId") UUID clientId,
                                @Param("accion") String accion,
                                @Param("from") Instant from,
                                @Param("to") Instant to);
}
