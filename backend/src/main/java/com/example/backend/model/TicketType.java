package com.example.backend.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TicketType {
    STANDARD("standard", new BigDecimal("49")),
    VIP("vip", new BigDecimal("99"));

    static final int DISCOUNT_FROM_QUANTITY = 3;
    static final BigDecimal DISCOUNT_RATE = new BigDecimal("0.10");

    private final String value;
    private final BigDecimal unitPrice;

    TicketType(String value, BigDecimal unitPrice) {
        this.value = value;
        this.unitPrice = unitPrice;
    }

    @JsonValue
    public String value() {
        return value;
    }

    public BigDecimal unitPrice() {
        return unitPrice;
    }

    public BigDecimal priceFor(int quantity) {
        BigDecimal subtotal = unitPrice.multiply(BigDecimal.valueOf(quantity));
        if (quantity < DISCOUNT_FROM_QUANTITY) {
            return subtotal.setScale(2, RoundingMode.HALF_UP);
        }
        BigDecimal discount = subtotal.multiply(DISCOUNT_RATE).setScale(2, RoundingMode.HALF_UP);
        return subtotal.subtract(discount).setScale(2, RoundingMode.HALF_UP);
    }

    @JsonCreator
    public static TicketType fromValue(String value) {
        for (TicketType type : values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unbekannte Ticketart: " + value);
    }
}
