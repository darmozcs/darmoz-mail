package com.darmoz.mail.scheduledemail;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface ScheduledEmailRepository extends JpaRepository<ScheduledEmail, Long> {

    List<ScheduledEmail> findByStatus(ScheduledEmailStatus status);

    @Query("select e from ScheduledEmail e where e.status = com.darmoz.mail.scheduledemail.ScheduledEmailStatus.PENDING "
            + "and e.scheduledAt <= :now order by e.scheduledAt asc")
    List<ScheduledEmail> findDueBatch(@Param("now") Instant now, Pageable pageable);
}
