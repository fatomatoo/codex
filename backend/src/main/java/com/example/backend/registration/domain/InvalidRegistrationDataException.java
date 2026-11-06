package com.example.backend.registration.domain;

/** Thrown when a value would violate an invariant of the registration model. */
public class InvalidRegistrationDataException extends IllegalArgumentException {

    public InvalidRegistrationDataException(String message) {
        super(message);
    }
}
