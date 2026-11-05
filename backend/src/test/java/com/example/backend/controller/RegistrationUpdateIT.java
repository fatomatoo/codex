package com.example.backend.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.client.RestTestClient;

import com.example.backend.TestcontainersConfiguration;
import com.example.backend.model.Registration;
import com.example.backend.model.TicketType;
import com.example.backend.repository.RegistrationRepository;

/**
 * Boots the whole application against a real PostgreSQL container and calls the
 * update endpoint over HTTP: controller, validation, service, JPA and database.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = "app.seed-data=false")
@AutoConfigureRestTestClient
@Import(TestcontainersConfiguration.class)
class RegistrationUpdateIT {

    @Autowired
    RestTestClient client;

    @Autowired
    RegistrationRepository repository;

    @Test
    void updatesRegistrationAndPersistsItInPostgres() {
        Registration saved = save("Ada Lovelace", "ada@example.com");

        put(saved.getId(), "Ada King", "ada.king@example.com", "vip", 3)
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.name").isEqualTo("Ada King")
                .jsonPath("$.ticketType").isEqualTo("vip");

        Registration reloaded = repository.findById(saved.getId()).orElseThrow();
        assertThat(reloaded.getName()).isEqualTo("Ada King");
        assertThat(reloaded.getEmail()).isEqualTo("ada.king@example.com");
        assertThat(reloaded.getTicketType()).isEqualTo(TicketType.VIP);
        assertThat(reloaded.getQuantity()).isEqualTo(3);
        // 3 x 99 with 10% discount
        assertThat(reloaded.getTotal()).isEqualByComparingTo("267.30");
    }

    @Test
    void returnsNotFoundForUnknownId() {
        put("does-not-exist", "Nobody", "nobody@example.com", "standard", 1)
                .expectStatus().isNotFound();
    }

    @Test
    void returnsConflictWhenEmailBelongsToAnotherRegistration() {
        save("Alan Turing", "alan@example.com");
        Registration other = save("Grace Hopper", "grace@example.com");

        put(other.getId(), "Grace Hopper", "ALAN@example.com", "standard", 1)
                .expectStatus().isEqualTo(409);

        assertThat(repository.findById(other.getId()).orElseThrow().getEmail()).isEqualTo("grace@example.com");
    }

    @Test
    void rejectsInvalidPayload() {
        Registration saved = save("Linus", "linus@example.com");

        put(saved.getId(), "Linus", "linus@example.com", "standard", 9)
                .expectStatus().isBadRequest();
    }

    private Registration save(String name, String email) {
        return repository.save(new Registration(name, email, TicketType.STANDARD, 1, new BigDecimal("49.00")));
    }

    private RestTestClient.ResponseSpec put(String id, String name, String email, String ticketType, int quantity) {
        return client.put()
                .uri("/api/registrations/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of("name", name, "email", email, "ticketType", ticketType, "quantity", quantity))
                .exchange();
    }
}
