package com.example.backend.registration.application;

import com.example.backend.registration.domain.Email;
import com.example.backend.registration.domain.Quantity;
import com.example.backend.registration.domain.TicketType;

public record RegistrationDetails(Email email, TicketType ticketType, Quantity quantity) {
}
