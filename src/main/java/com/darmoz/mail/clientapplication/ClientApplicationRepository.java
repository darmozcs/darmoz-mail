package com.darmoz.mail.clientapplication;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ClientApplicationRepository extends JpaRepository<ClientApplication, UUID> {

    List<ClientApplication> findByActive(boolean active);

    boolean existsByIdAndActiveTrue(UUID id);
}
