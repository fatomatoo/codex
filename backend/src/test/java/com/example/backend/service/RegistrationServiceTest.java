package com.example.backend.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.backend.dto.RegistrationRequest;
import com.example.backend.dto.RegistrationResponse;
import com.example.backend.model.Registration;
import com.example.backend.model.TicketType;
import com.example.backend.repository.RegistrationRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;

/**
 * Typical mock-heavy test: it mostly restates the implementation (see the review notes in the chat).
 */
class RegistrationServiceTest {

  private final RegistrationRepository repository = mock(RegistrationRepository.class);
  private final RegistrationService service = new RegistrationService(repository);

  @Test
  void updateReturns() {
    Registration existing = new Registration("Ada", "ada@example.com", TicketType.STANDARD, 1);

    when(repository.findById("1")).thenReturn(Optional.of(existing));
    when(repository.existsByEmailIgnoreCaseAndIdNot("ada.king@example.com", "1")).thenReturn(false);
    when(repository.saveAndFlush(any(Registration.class))).thenReturn(existing);

    RegistrationResponse response = service.update("1",
      new RegistrationRequest("Ada King", "ada.king@example.com", TicketType.VIP, 3));

    assertThat(response.total()).isEqualByComparingTo("267.30");
    verify(repository).saveAndFlush(existing);
  }
}
