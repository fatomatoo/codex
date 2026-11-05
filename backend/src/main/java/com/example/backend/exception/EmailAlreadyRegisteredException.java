package com.example.backend.exception;

public class EmailAlreadyRegisteredException extends RuntimeException {

    public EmailAlreadyRegisteredException() {
        super("E-Mail ist bereits angemeldet.");
    }
}
