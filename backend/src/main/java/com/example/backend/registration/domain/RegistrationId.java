package com.example.backend.registration.domain;

import java.io.Serializable;
import java.util.UUID;

import jakarta.persistence.Embeddable;

@Embeddable
public record RegistrationId(String value) implements Serializable {

    public RegistrationId {
        if (value == null || value.isBlank()) {
            throw new InvalidRegistrationDataException("Registration id must not be blank");
        }
    }

    public static RegistrationId generate() {
        return new RegistrationId(UUID.randomUUID().toString());
    }

    @Override
    public String toString() {
        return value;
    }
}
