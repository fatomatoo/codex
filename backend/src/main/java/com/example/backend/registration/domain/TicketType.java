package com.example.backend.registration.domain;

import java.math.BigDecimal;

public enum TicketType {
    STANDARD(Money.euros("49")),
    VIP(Money.euros("99"));

    static final int DISCOUNT_FROM_QUANTITY = 3;
    static final BigDecimal DISCOUNT_RATE = new BigDecimal("0.10");

    private final Money unitPrice;

    TicketType(Money unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Money unitPrice() {
        return unitPrice;
    }

    /** Total price for the given number of tickets, including the bulk discount. */
    public Money priceFor(Quantity quantity) {
        Money subtotal = unitPrice.times(quantity.value());
        if (!quantity.isAtLeast(DISCOUNT_FROM_QUANTITY)) {
            return subtotal;
        }
        return subtotal.minus(subtotal.times(DISCOUNT_RATE));
    }
}
