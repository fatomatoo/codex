package com.example.backend.registration;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.stereotype.Component;

@Component
public class PriceCalculator {

    static final int DISCOUNT_FROM_QUANTITY = 3;
    static final BigDecimal DISCOUNT_RATE = new BigDecimal("0.10");

    public BigDecimal total(TicketType ticketType, int quantity) {
        BigDecimal subtotal = ticketType.unitPrice().multiply(BigDecimal.valueOf(quantity));
        if (quantity < DISCOUNT_FROM_QUANTITY) {
            return subtotal.setScale(2, RoundingMode.HALF_UP);
        }
        BigDecimal discount = subtotal.multiply(DISCOUNT_RATE).setScale(2, RoundingMode.HALF_UP);
        return subtotal.subtract(discount).setScale(2, RoundingMode.HALF_UP);
    }
}
