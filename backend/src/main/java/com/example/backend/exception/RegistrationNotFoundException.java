package com.example.backend.exception;

public class RegistrationNotFoundException extends RuntimeException {

    public RegistrationNotFoundException() {
        super("Anmeldung nicht gefunden.");
    }
}
