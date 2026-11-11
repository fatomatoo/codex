package com.example.backend.registration.domain;

import java.util.Objects;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

/** Aggregate root: an attendee's registration for the event. */
@Entity
@Table(name = "registration")
public class Registration {

    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "id"))
    private RegistrationId id;

    @Column(name = "email", nullable = false, unique = true)
    private Email email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TicketType ticketType;

    @Column(nullable = false)
    private Quantity quantity;

    protected Registration() {
    }

    private Registration(RegistrationId id, Email email, TicketType ticketType, Quantity quantity) {
        this.id = Objects.requireNonNull(id);
        this.email = Objects.requireNonNull(email);
        applyTickets(ticketType, quantity);
    }

    public static Registration register(Email email, TicketType ticketType, Quantity quantity) {
        return new Registration(RegistrationId.generate(), email, ticketType, quantity);
    }

    public void changeEmail(Email email) {
        this.email = Objects.requireNonNull(email);
    }

    public void changeTickets(TicketType ticketType, Quantity quantity) {
        applyTickets(ticketType, quantity);
    }

    private void applyTickets(TicketType ticketType, Quantity quantity) {
        this.ticketType = Objects.requireNonNull(ticketType);
        this.quantity = Objects.requireNonNull(quantity);
    }

    public RegistrationId getId() {
        return id;
    }

    public Email getEmail() {
        return email;
    }

    public TicketType getTicketType() {
        return ticketType;
    }

    public Quantity getQuantity() {
        return quantity;
    }
}
