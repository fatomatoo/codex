package com.example.backend.registration;

public class EmailAlreadyRegisteredException extends RuntimeException {

    public EmailAlreadyRegisteredException() {
        super("E-Mail ist bereits angemeldet.");
    }
}
