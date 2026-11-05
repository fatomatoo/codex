package com.example.backend.config;

import java.time.Instant;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import com.example.backend.model.Registration;
import com.example.backend.model.TicketType;
import com.example.backend.repository.RegistrationRepository;
import com.example.backend.service.PriceCalculator;

/** Fills the database with two example registrations on startup if it is empty. */
@Component
@ConditionalOnProperty(name = "app.seed-data", havingValue = "true", matchIfMissing = true)
public class SeedData implements CommandLineRunner {

    private final RegistrationRepository repository;
    private final PriceCalculator priceCalculator;

    public SeedData(RegistrationRepository repository, PriceCalculator priceCalculator) {
        this.repository = repository;
        this.priceCalculator = priceCalculator;
    }

    @Override
    public void run(String... args) {
        if (repository.count() > 0) {
            return;
        }
        save("Ada Lovelace", "ada@example.com", TicketType.VIP, 2, "2026-10-01T09:00:00Z");
        save("Alan Turing", "alan@example.com", TicketType.STANDARD, 3, "2026-10-02T14:30:00Z");
    }

    private void save(String name, String email, TicketType type, int quantity, String createdAt) {
        Registration registration = new Registration(name, email, type, quantity,
                priceCalculator.total(type, quantity));
        registration.setCreatedAt(Instant.parse(createdAt));
        repository.save(registration);
    }
}
