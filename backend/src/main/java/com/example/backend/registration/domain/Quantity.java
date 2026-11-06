package com.example.backend.registration.domain;

public record Quantity(int value) {

    public static final int MIN = 1;
    public static final int MAX = 5;

    public Quantity {
        if (value < MIN || value > MAX) {
            throw new InvalidRegistrationDataException(
                    "Quantity must be between " + MIN + " and " + MAX + " but was " + value);
        }
    }

    public boolean isAtLeast(int threshold) {
        return value >= threshold;
    }
}
