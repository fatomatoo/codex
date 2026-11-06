package com.example.backend.registration.domain;

public record AttendeeName(String value) {

    public static final int MIN_LENGTH = 2;

    public AttendeeName {
        if (value == null) {
            throw new InvalidRegistrationDataException("Name must not be null");
        }
        value = value.trim();
        if (value.length() < MIN_LENGTH) {
            throw new InvalidRegistrationDataException("Name must have at least " + MIN_LENGTH + " characters");
        }
    }

    @Override
    public String toString() {
        return value;
    }
}
