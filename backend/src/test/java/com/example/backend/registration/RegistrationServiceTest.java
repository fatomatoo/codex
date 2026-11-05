package com.example.backend.registration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.Test;

/** Typical mock-heavy test: it mostly restates the implementation (see the review notes in the chat). */
class RegistrationServiceTest {

    private final RegistrationRepository repository = mock(RegistrationRepository.class);
    private final PriceCalculator priceCalculator = mock(PriceCalculator.class);
    private final RegistrationService service = new RegistrationService(repository, priceCalculator);

    @Test
    void updateReturnsWhatTheRepositoryReturns() {
        Registration existing = new Registration("Ada", "ada@example.com", TicketType.STANDARD, 1, new BigDecimal("49.00"));
        when(repository.findById("1")).thenReturn(Optional.of(existing));
        when(repository.existsByEmailIgnoreCaseAndIdNot("ada.king@example.com", "1")).thenReturn(false);
        when(priceCalculator.total(TicketType.VIP, 3)).thenReturn(new BigDecimal("267.30"));
        when(repository.saveAndFlush(any(Registration.class))).thenReturn(existing);

        RegistrationResponse response = service.update("1",
                new RegistrationRequest("Ada King", "ada.king@example.com", TicketType.VIP, 3));

        assertThat(response.total()).isEqualByComparingTo("267.30");
        verify(repository).saveAndFlush(existing);
    }
}
