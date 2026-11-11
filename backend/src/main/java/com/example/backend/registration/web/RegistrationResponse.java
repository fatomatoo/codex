package com.example.backend.registration.web;

import com.example.backend.registration.domain.Registration;

public record RegistrationResponse(
        String id,
        String email,
        TicketTypeDto ticketType,
        int quantity) {

    static RegistrationResponse from(Registration registration) {
        return new RegistrationResponse(
                registration.getId().value(),
                registration.getEmail().value(),
                TicketTypeDto.from(registration.getTicketType()),
                registration.getQuantity().value());
    }
}
