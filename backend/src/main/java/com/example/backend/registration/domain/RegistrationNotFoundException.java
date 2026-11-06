package com.example.backend.registration.domain;

public class RegistrationNotFoundException extends RuntimeException {

    public RegistrationNotFoundException(RegistrationId id) {
        super("Registration not found: " + id);
    }
}
