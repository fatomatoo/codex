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
class RegistrationUpdate_IntegrationTest {

  @Autowired
  RestTestClient client;

  @Test
  void updatedRegistrationIsRetrievedWithNewValues() {
    RegistrationResponse existing = create("ada@example.com", "standard", 1);

    put(existing.id(), "ada.king@example.com", "vip", 3);

    RegistrationResponse updated = retrieve(existing.id());
    assertThat(updated.email()).isEqualTo("ada.king@example.com");
    assertThat(updated.ticketType().value()).isEqualTo("vip");
    assertThat(updated.quantity()).isEqualTo(3);
  }

  private RegistrationResponse retrieve(String id) {
    return list().stream()
      .filter(registration -> registration.id().equals(id))
      .findFirst().orElseThrow();
  }

  @Test
  void keepsOwnEmailOnUpdate() {
    RegistrationResponse created = create("linus@example.com", "standard", 1);

    put(created.id(), "linus@example.com", "standard", 2)
      .expectStatus().isOk()
      .expectBody().jsonPath("$.quantity").isEqualTo(2);
  }

  @Test
  void returnsNotFoundForUnknownId() {
    put("does-not-exist", "nobody@example.com", "standard", 1)
      .expectStatus().isNotFound();
  }

  @Test
  void returnsConflictWhenEmailBelongsToAnotherRegistration() {
    create("alan@example.com", "standard", 1);
    RegistrationResponse other = create("grace@example.com", "standard", 1);

    put(other.id(), "ALAN@example.com", "standard", 1)
      .expectStatus().isEqualTo(409);

    assertThat(list()).filteredOn(r -> r.id().equals(other.id()))
      .singleElement().extracting(RegistrationResponse::email).isEqualTo("grace@example.com");
  }

  @Test
  void rejectsInvalidPayloadAndLeavesRegistrationUnchanged() {
    RegistrationResponse created = create("margaret@example.com", "standard", 1);

    put(created.id(), "margaret@example.com", "standard", 9)
      .expectStatus().isBadRequest();

    assertThat(list()).filteredOn(r -> r.id().equals(created.id()))
      .singleElement().extracting(RegistrationResponse::quantity).isEqualTo(1);
  }

  private RegistrationResponse create(String email, String ticketType, int quantity) {
    return client.post().uri("/api/registrations")
      .contentType(MediaType.APPLICATION_JSON)
      .body(payload(email, ticketType, quantity))
      .exchange()
      .expectStatus().isCreated()
      .expectBody(RegistrationResponse.class)
      .returnResult().getResponseBody();
  }

  private RestTestClient.ResponseSpec put(String id, String email, String ticketType, int quantity) {
    return client.put().uri("/api/registrations/{id}", id)
      .contentType(MediaType.APPLICATION_JSON)
      .body(payload(email, ticketType, quantity))
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

  private static Map<String, Object> payload(String email, String ticketType, int quantity) {
    return Map.of("email", email, "ticketType", ticketType, "quantity", quantity);
  }
}
