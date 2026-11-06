package com.example.backend.registration.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.backend.registration.domain.AttendeeName;
import com.example.backend.registration.domain.Email;
import com.example.backend.registration.domain.Quantity;
import com.example.backend.registration.domain.Registration;
import com.example.backend.registration.domain.RegistrationId;
import com.example.backend.registration.domain.RegistrationRepository;
import com.example.backend.registration.domain.TicketType;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Optional;
import org.junit.jupiter.api.Test;

/**
 * Typical mock-heavy test: it mostly restates the implementation (see the review notes in the chat).
 */
class RegistrationServiceTest {

  private final RegistrationRepository repository = mock(RegistrationRepository.class);
  private final Clock clock = Clock.fixed(Instant.parse("2026-11-01T10:00:00Z"), ZoneOffset.UTC);
  private final RegistrationService service = new RegistrationService(repository, clock);

  @Test
  void updateReturns() {
    Registration existing = Registration.register(new AttendeeName("Ada"), new Email("ada@example.com"),
      TicketType.STANDARD, new Quantity(1), clock.instant());
    RegistrationId id = new RegistrationId("1");
    Email newEmail = new Email("ada.king@example.com");

    when(repository.findById(id)).thenReturn(Optional.of(existing));
    when(repository.existsByEmailExcept(newEmail, id)).thenReturn(false);
    when(repository.save(any(Registration.class))).thenReturn(existing);

    Registration result = service.change(id,
      new RegistrationDetails(new AttendeeName("Ada King"), newEmail, TicketType.VIP, new Quantity(3)));

    assertThat(result.getTotal().amount()).isEqualByComparingTo("267.30");
    verify(repository).save(existing);
  }
}
