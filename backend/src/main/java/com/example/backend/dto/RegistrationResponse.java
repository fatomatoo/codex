package com.example.backend.dto;

import java.math.BigDecimal;
import java.time.Instant;

import com.example.backend.model.Registration;
import com.example.backend.model.TicketType;

public record RegistrationResponse(
        String id,
        String name,
        String email,
        TicketType ticketType,
        int quantity,
        BigDecimal total,
        Instant createdAt) {

    public static RegistrationResponse from(Registration registration) {
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
