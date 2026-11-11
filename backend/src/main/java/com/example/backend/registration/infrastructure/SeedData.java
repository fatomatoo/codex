package com.example.backend.registration.infrastructure;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

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
        save("ada@example.com", TicketType.VIP, 2);
        save("alan@example.com", TicketType.STANDARD, 3);
    }

    private void save(String email, TicketType type, int quantity) {
        repository.save(Registration.register(new Email(email), type, new Quantity(quantity)));
    }
}
