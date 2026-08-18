package com.darmoz.mail.clientapplication;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/client-applications")
public class ClientApplicationController {

    private final ClientApplicationService clientApplicationService;

    ClientApplicationController(ClientApplicationService clientApplicationService) {
        this.clientApplicationService = clientApplicationService;
    }

    @PostMapping
    ResponseEntity<ClientApplicationResponse> create(@Valid @RequestBody ClientApplicationCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(clientApplicationService.create(request));
    }

    @PatchMapping("/{id}")
    ClientApplicationResponse update(@PathVariable UUID id, @RequestBody ClientApplicationUpdateRequest request) {
        return clientApplicationService.update(id, request);
    }

    @GetMapping("/{id}")
    ClientApplicationResponse get(@PathVariable UUID id) {
        return clientApplicationService.getById(id);
    }

    @GetMapping
    List<ClientApplicationResponse> list(@RequestParam(required = false) Boolean active) {
        return clientApplicationService.list(active);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable UUID id) {
        clientApplicationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
