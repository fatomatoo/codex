package com.example.backend.service;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.backend.dto.RegistrationRequest;
import com.example.backend.dto.RegistrationResponse;
import com.example.backend.exception.EmailAlreadyRegisteredException;
import com.example.backend.exception.RegistrationNotFoundException;
import com.example.backend.model.Registration;
import com.example.backend.repository.RegistrationRepository;

@Service
public class RegistrationService {

    private final RegistrationRepository repository;

    public RegistrationService(RegistrationRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<RegistrationResponse> findAll() {
        return repository.findAllByOrderByCreatedAtDesc().stream()
                .map(RegistrationResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public boolean isEmailTaken(String email) {
        return repository.existsByEmailIgnoreCase(Registration.normalizeEmail(email));
    }

    @Transactional
    public RegistrationResponse create(RegistrationRequest request) {
        String email = Registration.normalizeEmail(request.email());
        if (repository.existsByEmailIgnoreCase(email)) {
            throw new EmailAlreadyRegisteredException();
        }
        Registration registration = new Registration(
                request.name(), email, request.ticketType(), request.quantity());
        try {
            return RegistrationResponse.from(repository.saveAndFlush(registration));
        } catch (DataIntegrityViolationException e) {
            throw new EmailAlreadyRegisteredException();
        }
    }

    @Transactional
    public RegistrationResponse update(String id, RegistrationRequest request) {
        Registration registration = repository.findById(id).orElseThrow(RegistrationNotFoundException::new);
        String email = Registration.normalizeEmail(request.email());
        if (repository.existsByEmailIgnoreCaseAndIdNot(email, id)) {
            throw new EmailAlreadyRegisteredException();
        }
        registration.update(request.name(), email, request.ticketType(), request.quantity());
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
