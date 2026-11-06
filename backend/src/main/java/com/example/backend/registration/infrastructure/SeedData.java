package com.example.backend.registration.infrastructure;

import java.time.Instant;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import com.example.backend.registration.domain.AttendeeName;
import com.example.backend.registration.domain.Email;
import com.example.backend.registration.domain.Quantity;
import com.example.backend.registration.domain.Registration;
import com.example.backend.registration.domain.RegistrationRepository;
import com.example.backend.registration.domain.TicketType;

/** Fills the database with two example registrations on startup if it is empty. */
@Component
@ConditionalOnProperty(name = "app.seed-data", havingValue = "true", matchIfMissing = true)
class SeedData implements CommandLineRunner {

    private final RegistrationRepository repository;

    SeedData(RegistrationRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) {
        if (!repository.isEmpty()) {
            return;
        }
        save("Ada Lovelace", "ada@example.com", TicketType.VIP, 2, "2026-10-01T09:00:00Z");
        save("Alan Turing", "alan@example.com", TicketType.STANDARD, 3, "2026-10-02T14:30:00Z");
    }

    private void save(String name, String email, TicketType type, int quantity, String registeredAt) {
        repository.save(Registration.register(new AttendeeName(name), new Email(email), type,
                new Quantity(quantity), Instant.parse(registeredAt)));
    }
}
