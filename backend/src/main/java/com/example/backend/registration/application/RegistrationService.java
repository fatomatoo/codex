package com.example.backend.registration.application;

import java.time.Clock;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.backend.registration.domain.Email;
import com.example.backend.registration.domain.EmailAlreadyRegisteredException;
import com.example.backend.registration.domain.Registration;
import com.example.backend.registration.domain.RegistrationId;
import com.example.backend.registration.domain.RegistrationNotFoundException;
import com.example.backend.registration.domain.RegistrationRepository;

@Service
public class RegistrationService {

    private final RegistrationRepository repository;
    private final Clock clock;

    public RegistrationService(RegistrationRepository repository, Clock clock) {
        this.repository = repository;
        this.clock = clock;
    }

    @Transactional(readOnly = true)
    public List<Registration> findAll() {
        return repository.findAllNewestFirst();
    }

    @Transactional(readOnly = true)
    public boolean isEmailTaken(Email email) {
        return repository.existsByEmail(email);
    }

    @Transactional
    public Registration register(RegistrationDetails details) {
        if (repository.existsByEmail(details.email())) {
            throw new EmailAlreadyRegisteredException(details.email());
        }
        return repository.save(Registration.register(
                details.name(), details.email(), details.ticketType(), details.quantity(), clock.instant()));
    }

    @Transactional
    public Registration change(RegistrationId id, RegistrationDetails details) {
        Registration registration = repository.findById(id)
                .orElseThrow(() -> new RegistrationNotFoundException(id));
        if (repository.existsByEmailExcept(details.email(), id)) {
            throw new EmailAlreadyRegisteredException(details.email());
        }
        registration.changeContactDetails(details.name(), details.email());
        registration.changeTickets(details.ticketType(), details.quantity());
        return repository.save(registration);
    }

    @Transactional
    public void cancel(RegistrationId id) {
        Registration registration = repository.findById(id)
                .orElseThrow(() -> new RegistrationNotFoundException(id));
        repository.delete(registration);
    }
}
