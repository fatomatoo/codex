package com.example.backend.registration.web;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.backend.TestcontainersConfiguration;
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

/**
 * Black-box test of the public REST API: the whole application runs against a real PostgreSQL container, and every step and assertion goes through HTTP only.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = "app.seed-data=false")
@AutoConfigureRestTestClient
@Import(TestcontainersConfiguration.class)
class RegistrationUpdateIntegrationTest {

  @Autowired
  RestTestClient client;

  @Test
  void updatedRegistrationIsRetrievedWithNewValuesAndRecalculatedTotal() {
    RegistrationResponse existing = create("Ada Lovelace", "ada@example.com", "standard", 1);

    put(existing.id(), "Ada King", "ada.king@example.com", "vip", 3);

    RegistrationResponse updated = retrieve(existing.id());
    assertThat(updated.name()).isEqualTo("Ada King");
    assertThat(updated.email()).isEqualTo("ada.king@example.com");
    assertThat(updated.ticketType().value()).isEqualTo("vip");
    assertThat(updated.quantity()).isEqualTo(3);
    assertThat(updated.total()).isEqualByComparingTo("267.30"); // 3 x 99 minus 10% discount
  }

  private RegistrationResponse retrieve(String id) {
    return list().stream()
      .filter(registration -> registration.id().equals(id))
      .findFirst().orElseThrow();
  }

  @Test
  void keepsOwnEmailOnUpdate() {
    RegistrationResponse created = create("Linus", "linus@example.com", "standard", 1);

    put(created.id(), "Linus T", "linus@example.com", "standard", 2)
      .expectStatus().isOk()
      .expectBody().jsonPath("$.name").isEqualTo("Linus T");
  }

  @Test
  void returnsNotFoundForUnknownId() {
    put("does-not-exist", "Nobody", "nobody@example.com", "standard", 1)
      .expectStatus().isNotFound();
  }

  @Test
  void returnsConflictWhenEmailBelongsToAnotherRegistration() {
    create("Alan Turing", "alan@example.com", "standard", 1);
    RegistrationResponse other = create("Grace Hopper", "grace@example.com", "standard", 1);

    put(other.id(), "Grace Hopper", "ALAN@example.com", "standard", 1)
      .expectStatus().isEqualTo(409);

    assertThat(list()).filteredOn(r -> r.id().equals(other.id()))
      .singleElement().extracting(RegistrationResponse::email).isEqualTo("grace@example.com");
  }

  @Test
  void rejectsInvalidPayloadAndLeavesRegistrationUnchanged() {
    RegistrationResponse created = create("Margaret", "margaret@example.com", "standard", 1);

    put(created.id(), "Margaret", "margaret@example.com", "standard", 9)
      .expectStatus().isBadRequest();

    assertThat(list()).filteredOn(r -> r.id().equals(created.id()))
      .singleElement().extracting(RegistrationResponse::quantity).isEqualTo(1);
  }

  private RegistrationResponse create(String name, String email, String ticketType, int quantity) {
    return client.post().uri("/api/registrations")
      .contentType(MediaType.APPLICATION_JSON)
      .body(payload(name, email, ticketType, quantity))
      .exchange()
      .expectStatus().isCreated()
      .expectBody(RegistrationResponse.class)
      .returnResult().getResponseBody();
  }

  private RestTestClient.ResponseSpec put(String id, String name, String email, String ticketType, int quantity) {
    return client.put().uri("/api/registrations/{id}", id)
      .contentType(MediaType.APPLICATION_JSON)
      .body(payload(name, email, ticketType, quantity))
      .exchange();
  }

  private List<RegistrationResponse> list() {
    return client.get().uri("/api/registrations")
      .exchange()
      .expectStatus().isOk()
      .expectBody(new ParameterizedTypeReference<List<RegistrationResponse>>() {
      })
      .returnResult().getResponseBody();
  }

  private static Map<String, Object> payload(String name, String email, String ticketType, int quantity) {
    return Map.of("name", name, "email", email, "ticketType", ticketType, "quantity", quantity);
  }
}
