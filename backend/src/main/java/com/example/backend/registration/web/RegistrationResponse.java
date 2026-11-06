package com.example.backend.registration.web;

import java.math.BigDecimal;
import java.time.Instant;

import com.example.backend.registration.domain.Registration;

public record RegistrationResponse(
        String id,
        String name,
        String email,
        TicketTypeDto ticketType,
        int quantity,
        BigDecimal total,
        Instant createdAt) {

    static RegistrationResponse from(Registration registration) {
        return new RegistrationResponse(
                registration.getId().value(),
                registration.getName().value(),
                registration.getEmail().value(),
                TicketTypeDto.from(registration.getTicketType()),
                registration.getQuantity().value(),
                registration.getTotal().amount(),
                registration.getCreatedAt());
    }
}
