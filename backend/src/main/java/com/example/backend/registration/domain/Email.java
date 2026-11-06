package com.example.backend.registration.domain;

import java.util.Locale;
import java.util.Optional;
import java.util.regex.Pattern;

/** Email address, normalized (trimmed, lower case) so that equality is case-insensitive. */
public record Email(String value) {

    private static final Pattern FORMAT = Pattern.compile("^[^@\\s]+@[^@\\s]+$");

    public Email {
        if (value == null) {
            throw new InvalidRegistrationDataException("Email must not be null");
        }
        value = value.trim().toLowerCase(Locale.ROOT);
        if (!FORMAT.matcher(value).matches()) {
            throw new InvalidRegistrationDataException("Invalid email address: " + value);
        }
    }

    public static Optional<Email> tryParse(String value) {
        try {
            return Optional.of(new Email(value));
        } catch (InvalidRegistrationDataException e) {
            return Optional.empty();
        }
    }

    @Override
    public String toString() {
        return value;
    }
}
