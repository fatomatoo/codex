package com.example.backend.registration.web;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.backend.CleanDatabaseExtension;
import com.example.backend.TestcontainersConfiguration;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureRestTestClient
@Import(TestcontainersConfiguration.class)
@ExtendWith(CleanDatabaseExtension.class)
class RegistrationChange_IntegrationTest {

  @Autowired
  RestTestClient client;

  @Test
  void changedRegistrationIsRetrievedWithNewValues() {
    RegistrationResponse existing = register("ada@example.com", "standard", 1);

    change(existing.id(), "ada.king@example.com", "vip", 3);

    RegistrationResponse changed = retrieve(existing.id());
    assertThat(changed.email()).isEqualTo("ada.king@example.com");
    assertThat(changed.ticketType().value()).isEqualTo("vip");
    assertThat(changed.quantity()).isEqualTo(3);
  }

  private RegistrationResponse register(String email, String ticketType, int quantity) {
    return client.post().uri("/api/registrations")
      .contentType(MediaType.APPLICATION_JSON)
      .body(payload(email, ticketType, quantity))
      .exchange()
      .expectStatus().isCreated()
      .expectBody(RegistrationResponse.class)
      .returnResult().getResponseBody();
  }

  private RestTestClient.ResponseSpec change(String id, String email, String ticketType, int quantity) {
    return client.put().uri("/api/registrations/{id}", id)
      .contentType(MediaType.APPLICATION_JSON)
      .body(payload(email, ticketType, quantity))
      .exchange();
  }

  private RegistrationResponse retrieve(String id) {
    return list().stream()
      .filter(registration -> registration.id().equals(id))
      .findFirst().orElseThrow();
  }

  @Test
  void keepsOwnEmailOnChange() {
    RegistrationResponse registered = register("linus@example.com", "standard", 1);

    change(registered.id(), "linus@example.com", "standard", 2)
      .expectStatus().isOk()
      .expectBody().jsonPath("$.quantity").isEqualTo(2);
  }

  @Test
  void returnsNotFoundForUnknownId() {
    change("does-not-exist", "nobody@example.com", "standard", 1)
      .expectStatus().isNotFound();
  }

  @Test
  void returnsConflictWhenEmailBelongsToAnotherRegistration() {
    register("alan@example.com", "standard", 1);
    RegistrationResponse other = register("grace@example.com", "standard", 1);

    change(other.id(), "ALAN@example.com", "standard", 1)
      .expectStatus().isEqualTo(409);

    assertThat(list()).filteredOn(r -> r.id().equals(other.id()))
      .singleElement().extracting(RegistrationResponse::email).isEqualTo("grace@example.com");
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
