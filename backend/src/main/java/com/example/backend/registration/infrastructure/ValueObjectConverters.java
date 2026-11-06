package com.example.backend.registration.infrastructure;

import java.math.BigDecimal;

import com.example.backend.registration.domain.AttendeeName;
import com.example.backend.registration.domain.Email;
import com.example.backend.registration.domain.Money;
import com.example.backend.registration.domain.Quantity;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/** Maps the single-value domain types to plain columns, keeping JPA details out of the domain. */
final class ValueObjectConverters {

    private ValueObjectConverters() {
    }

    @Converter(autoApply = true)
    static class AttendeeNameConverter implements AttributeConverter<AttendeeName, String> {
        @Override
        public String convertToDatabaseColumn(AttendeeName name) {
            return name == null ? null : name.value();
        }

        @Override
        public AttendeeName convertToEntityAttribute(String value) {
            return value == null ? null : new AttendeeName(value);
        }
    }

    @Converter(autoApply = true)
    static class EmailConverter implements AttributeConverter<Email, String> {
        @Override
        public String convertToDatabaseColumn(Email email) {
            return email == null ? null : email.value();
        }

        @Override
        public Email convertToEntityAttribute(String value) {
            return value == null ? null : new Email(value);
        }
    }

    @Converter(autoApply = true)
    static class QuantityConverter implements AttributeConverter<Quantity, Integer> {
        @Override
        public Integer convertToDatabaseColumn(Quantity quantity) {
            return quantity == null ? null : quantity.value();
        }

        @Override
        public Quantity convertToEntityAttribute(Integer value) {
            return value == null ? null : new Quantity(value);
        }
    }

    @Converter(autoApply = true)
    static class MoneyConverter implements AttributeConverter<Money, BigDecimal> {
        @Override
        public BigDecimal convertToDatabaseColumn(Money money) {
            return money == null ? null : money.amount();
        }

        @Override
        public Money convertToEntityAttribute(BigDecimal value) {
            return value == null ? null : new Money(value);
        }
    }
}
