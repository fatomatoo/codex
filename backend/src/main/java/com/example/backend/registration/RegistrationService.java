package com.example.backend.registration;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegistrationService {

    private final RegistrationRepository repository;
    private final PriceCalculator priceCalculator;

    public RegistrationService(RegistrationRepository repository, PriceCalculator priceCalculator) {
        this.repository = repository;
        this.priceCalculator = priceCalculator;
    }

    @Transactional(readOnly = true)
    public List<RegistrationResponse> findAll() {
        return repository.findAllByOrderByCreatedAtDesc().stream()
                .map(RegistrationResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public boolean isEmailTaken(String email) {
        return repository.existsByEmailIgnoreCase(email.trim());
    }

    @Transactional
    public RegistrationResponse create(RegistrationRequest request) {
        String email = request.email().trim();
        if (repository.existsByEmailIgnoreCase(email)) {
            throw new EmailAlreadyRegisteredException();
        }
        Registration registration = new Registration(
                request.name().trim(),
                email,
                request.ticketType(),
                request.quantity(),
                priceCalculator.total(request.ticketType(), request.quantity()));
        try {
            return RegistrationResponse.from(repository.saveAndFlush(registration));
        } catch (DataIntegrityViolationException e) {
            throw new EmailAlreadyRegisteredException();
        }
    }

    @Transactional
    public void delete(String id) {
        if (!repository.existsById(id)) {
            throw new RegistrationNotFoundException();
        }
        repository.deleteById(id);
    }
}
