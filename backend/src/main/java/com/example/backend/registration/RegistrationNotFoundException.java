package com.example.backend.registration;

public class RegistrationNotFoundException extends RuntimeException {

    public RegistrationNotFoundException() {
        super("Anmeldung nicht gefunden.");
    }
}
