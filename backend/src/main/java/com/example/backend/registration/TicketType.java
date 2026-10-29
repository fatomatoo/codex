package com.example.backend.registration;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TicketType {
    STANDARD("standard", new BigDecimal("49")),
    VIP("vip", new BigDecimal("99"));

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
