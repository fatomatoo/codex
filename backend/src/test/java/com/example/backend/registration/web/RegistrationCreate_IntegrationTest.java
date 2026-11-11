package com.example.backend.registration.web;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.client.RestTestClient;

import com.example.backend.TestcontainersConfiguration;

/**
 * Same Spring configuration as {@link RegistrationUpdate_IntegrationTest}, so both classes share one
 * cached application context and one PostgreSQL container.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = "app.seed-data=false")
@AutoConfigureRestTestClient
@Import(TestcontainersConfiguration.class)
class RegistrationCreate_IntegrationTest {

    @Autowired
    RestTestClient client;

    @Test
    void createdRegistrationIsRetrieved() {
        post("katherine@example.com", "standard", 3);

        RegistrationResponse created = retrieveByEmail("katherine@example.com");
        assertThat(created.ticketType().value()).isEqualTo("standard");
        assertThat(created.quantity()).isEqualTo(3);
    }

    @Test
    void registrationWithTakenEmailIsRejectedAndNotStoredTwice() {
        post("dorothy@example.com", "standard", 1);

        post("DOROTHY@example.com", "vip", 1)
                .expectStatus().isEqualTo(409);

        assertThat(list()).filteredOn(r -> r.email().equalsIgnoreCase("dorothy@example.com")).hasSize(1);
    }

    @Test
    void invalidRegistrationIsRejectedAndNotStored() {
        post("mary@example.com", "standard", 0)
                .expectStatus().isBadRequest();

        assertThat(list()).noneMatch(r -> r.email().equals("mary@example.com"));
    }

    private RegistrationResponse retrieveByEmail(String email) {
        return list().stream()
                .filter(registration -> registration.email().equals(email))
                .findFirst().orElseThrow();
    }

    private RestTestClient.ResponseSpec post(String email, String ticketType, int quantity) {
        return client.post().uri("/api/registrations")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of("email", email, "ticketType", ticketType, "quantity", quantity))
                .exchange();
    }

    private List<RegistrationResponse> list() {
        return client.get().uri("/api/registrations")
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<List<RegistrationResponse>>() { })
                .returnResult().getResponseBody();
    }
}
