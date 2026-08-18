package com.darmoz.mail.clientapplication;

import com.darmoz.mail.common.ClientApplicationNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class ClientApplicationService {

    private final ClientApplicationRepository clientApplicationRepository;

    ClientApplicationService(ClientApplicationRepository clientApplicationRepository) {
        this.clientApplicationRepository = clientApplicationRepository;
    }

    @Transactional
    public ClientApplicationResponse create(ClientApplicationCreateRequest request) {
        ClientApplication clientApplication = new ClientApplication();
        clientApplication.setName(request.name());
        clientApplication.setActive(request.active() == null || request.active());

        return ClientApplicationResponse.from(clientApplicationRepository.save(clientApplication));
    }

    @Transactional
    public ClientApplicationResponse update(UUID id, ClientApplicationUpdateRequest request) {
        ClientApplication clientApplication = findOrThrow(id);

        if (request.name() != null && !request.name().isBlank()) {
            clientApplication.setName(request.name());
        }
        if (request.active() != null) {
            clientApplication.setActive(request.active());
        }

        return ClientApplicationResponse.from(clientApplicationRepository.save(clientApplication));
    }

    @Transactional(readOnly = true)
    public ClientApplicationResponse getById(UUID id) {
        return ClientApplicationResponse.from(findOrThrow(id));
    }

    @Transactional(readOnly = true)
    public List<ClientApplicationResponse> list(Boolean active) {
        List<ClientApplication> clientApplications = active == null
                ? clientApplicationRepository.findAll()
                : clientApplicationRepository.findByActive(active);

        return clientApplications.stream().map(ClientApplicationResponse::from).toList();
    }

    @Transactional
    public void delete(UUID id) {
        if (!clientApplicationRepository.existsById(id)) {
            throw new ClientApplicationNotFoundException(id);
        }
        clientApplicationRepository.deleteById(id);
    }

    private ClientApplication findOrThrow(UUID id) {
        return clientApplicationRepository.findById(id)
                .orElseThrow(() -> new ClientApplicationNotFoundException(id));
    }
}
