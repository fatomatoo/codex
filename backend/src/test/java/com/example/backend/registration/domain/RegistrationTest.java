package com.example.backend.registration.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.Instant;

import org.junit.jupiter.api.Test;

class RegistrationTest {

    private static final Instant NOW = Instant.parse("2026-11-01T10:00:00Z");

    @Test
    void newRegistrationCalculatesTotalWithoutDiscountBelowThreeTickets() {
        Registration registration = register(TicketType.VIP, 2);

        assertThat(registration.getTotal()).isEqualTo(Money.euros("198.00"));
        assertThat(registration.getCreatedAt()).isEqualTo(NOW);
    }

    @Test
    void changingTicketsRecalculatesTotalIncludingBulkDiscount() {
        Registration registration = register(TicketType.STANDARD, 1);

        registration.changeTickets(TicketType.STANDARD, new Quantity(3));

        assertThat(registration.getTotal()).isEqualTo(Money.euros("132.30"));
    }

    @Test
    void emailIsNormalizedSoThatEqualityIgnoresCaseAndWhitespace() {
        assertThat(new Email(" Ada@Example.COM ")).isEqualTo(new Email("ada@example.com"));
        assertThat(Email.tryParse("not-an-email")).isEmpty();
    }

    @Test
    void nameIsTrimmedAndMustHaveAtLeastTwoCharacters() {
        assertThat(new AttendeeName("  Ada  ").value()).isEqualTo("Ada");
        assertThatThrownBy(() -> new AttendeeName(" A ")).isInstanceOf(InvalidRegistrationDataException.class);
    }

    @Test
    void quantityMustBeBetweenOneAndFive() {
        assertThatThrownBy(() -> new Quantity(0)).isInstanceOf(InvalidRegistrationDataException.class);
        assertThatThrownBy(() -> new Quantity(6)).isInstanceOf(InvalidRegistrationDataException.class);
    }

    private static Registration register(TicketType ticketType, int quantity) {
        return Registration.register(new AttendeeName("Ada"), new Email("ada@example.com"), ticketType,
                new Quantity(quantity), NOW);
    }
}
