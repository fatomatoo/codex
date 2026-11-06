package com.example.backend.registration.domain;

import java.time.Instant;
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

    @Column(name = "name", nullable = false)
    private AttendeeName name;

    @Column(name = "email", nullable = false, unique = true)
    private Email email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TicketType ticketType;

    @Column(nullable = false)
    private Quantity quantity;

    @Column(nullable = false)
    private Money total;

    @Column(nullable = false)
    private Instant createdAt;

    protected Registration() {
    }

    private Registration(RegistrationId id, AttendeeName name, Email email, TicketType ticketType,
            Quantity quantity, Instant createdAt) {
        this.id = Objects.requireNonNull(id);
        this.createdAt = Objects.requireNonNull(createdAt);
        applyContactDetails(name, email);
        applyTickets(ticketType, quantity);
    }

    public static Registration register(AttendeeName name, Email email, TicketType ticketType, Quantity quantity,
            Instant registeredAt) {
        return new Registration(RegistrationId.generate(), name, email, ticketType, quantity, registeredAt);
    }

    public void changeContactDetails(AttendeeName name, Email email) {
        applyContactDetails(name, email);
    }

    /** Changes the booked tickets; the total is always recalculated from the current prices. */
    public void changeTickets(TicketType ticketType, Quantity quantity) {
        applyTickets(ticketType, quantity);
    }

    private void applyContactDetails(AttendeeName name, Email email) {
        this.name = Objects.requireNonNull(name);
        this.email = Objects.requireNonNull(email);
    }

    private void applyTickets(TicketType ticketType, Quantity quantity) {
        this.ticketType = Objects.requireNonNull(ticketType);
        this.quantity = Objects.requireNonNull(quantity);
        this.total = ticketType.priceFor(quantity);
    }

    public RegistrationId getId() {
        return id;
    }

    public AttendeeName getName() {
        return name;
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

    public Money getTotal() {
        return total;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
