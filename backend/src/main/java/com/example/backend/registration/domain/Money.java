package com.example.backend.registration.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;

/** Non-negative amount in euros, always kept at two decimal places. */
public record Money(BigDecimal amount) {

    private static final int SCALE = 2;

    public Money {
        if (amount == null || amount.signum() < 0) {
            throw new InvalidRegistrationDataException("Amount must be zero or positive");
        }
        amount = amount.setScale(SCALE, RoundingMode.HALF_UP);
    }

    public static Money euros(String amount) {
        return new Money(new BigDecimal(amount));
    }

    public Money times(int factor) {
        return new Money(amount.multiply(BigDecimal.valueOf(factor)));
    }

    public Money times(BigDecimal factor) {
        return new Money(amount.multiply(factor));
    }

    public Money minus(Money other) {
        return new Money(amount.subtract(other.amount));
    }
}
