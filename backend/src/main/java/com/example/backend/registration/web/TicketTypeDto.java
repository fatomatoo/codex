package com.example.backend.registration.web;

import com.example.backend.registration.domain.TicketType;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/** JSON representation of {@link TicketType}, so the domain enum stays free of serialization concerns. */
public enum TicketTypeDto {
    STANDARD("standard", TicketType.STANDARD),
    VIP("vip", TicketType.VIP);

    private final String value;
    private final TicketType domain;

    TicketTypeDto(String value, TicketType domain) {
        this.value = value;
        this.domain = domain;
    }

    @JsonValue
    public String value() {
        return value;
    }

    public TicketType toDomain() {
        return domain;
    }

    public static TicketTypeDto from(TicketType ticketType) {
        for (TicketTypeDto dto : values()) {
            if (dto.domain == ticketType) {
                return dto;
            }
        }
        throw new IllegalArgumentException("No JSON value for ticket type " + ticketType);
    }

    @JsonCreator
    public static TicketTypeDto fromValue(String value) {
        for (TicketTypeDto dto : values()) {
            if (dto.value.equalsIgnoreCase(value)) {
                return dto;
            }
        }
        throw new IllegalArgumentException("Unknown ticket type: " + value);
    }
}
