package com.example.backend.registration;

import java.math.BigDecimal;
import java.time.Instant;

public record RegistrationResponse(
        String id,
        String name,
        String email,
        TicketType ticketType,
        int quantity,
        BigDecimal total,
        Instant createdAt) {

    static RegistrationResponse from(Registration registration) {
        return new RegistrationResponse(
                registration.getId(),
                registration.getName(),
                registration.getEmail(),
                registration.getTicketType(),
                registration.getQuantity(),
                registration.getTotal(),
                registration.getCreatedAt());
    }
}
