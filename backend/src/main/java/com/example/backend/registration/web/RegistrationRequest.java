package com.example.backend.registration.web;

import com.example.backend.registration.application.RegistrationDetails;
import com.example.backend.registration.domain.AttendeeName;
import com.example.backend.registration.domain.Email;
import com.example.backend.registration.domain.Quantity;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Bean Validation only produces user-friendly messages at the API boundary;
 * the invariants themselves are enforced by the domain value objects.
 */
public record RegistrationRequest(
        @NotBlank(message = "Bitte gib deinen Namen an.")
        @Size(min = AttendeeName.MIN_LENGTH, message = "Der Name braucht mindestens 2 Zeichen.")
        String name,

        @NotBlank(message = "Bitte gib deine E-Mail-Adresse an.")
        @jakarta.validation.constraints.Email(message = "Das ist keine gültige E-Mail-Adresse.")
        String email,

        @NotNull(message = "Bitte wähle eine Ticketart.")
        TicketTypeDto ticketType,

        @Min(value = Quantity.MIN, message = "Mindestens 1 Ticket.")
        @Max(value = Quantity.MAX, message = "Höchstens 5 Tickets.")
        int quantity) {

    RegistrationDetails toDetails() {
        return new RegistrationDetails(
                new AttendeeName(name), new Email(email), ticketType.toDomain(), new Quantity(quantity));
    }
}
