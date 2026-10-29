package com.example.backend.registration;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegistrationRequest(
        @NotBlank(message = "Bitte gib deinen Namen an.")
        @Size(min = 2, message = "Der Name braucht mindestens 2 Zeichen.")
        String name,

        @NotBlank(message = "Bitte gib deine E-Mail-Adresse an.")
        @Email(message = "Das ist keine gültige E-Mail-Adresse.")
        String email,

        @NotNull(message = "Bitte wähle eine Ticketart.")
        TicketType ticketType,

        @Min(value = 1, message = "Mindestens 1 Ticket.")
        @Max(value = 5, message = "Höchstens 5 Tickets.")
        int quantity) {
}
