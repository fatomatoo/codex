package com.example.backend.registration.domain;

public class EmailAlreadyRegisteredException extends RuntimeException {

    public EmailAlreadyRegisteredException(Email email) {
        super("Email already registered: " + email);
    }
}
