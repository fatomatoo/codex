package com.example.backend.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "registration")
public class Registration {

    @Id
    private String id = UUID.randomUUID().toString();

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TicketType ticketType;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private BigDecimal total;

    @Column(nullable = false)
    private Instant createdAt;

    protected Registration() {
    }

    public Registration(String name, String email, TicketType ticketType, int quantity) {
        this(name, email, ticketType, quantity, Instant.now());
    }

    public Registration(String name, String email, TicketType ticketType, int quantity, Instant createdAt) {
        apply(name, email, ticketType, quantity);
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public TicketType getTicketType() {
        return ticketType;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void update(String name, String email, TicketType ticketType, int quantity) {
        apply(name, email, ticketType, quantity);
    }

    private void apply(String name, String email, TicketType ticketType, int quantity) {
        this.name = name.trim();
        this.email = normalizeEmail(email);
        this.ticketType = ticketType;
        this.quantity = quantity;
        this.total = ticketType.priceFor(quantity);
    }

    public static String normalizeEmail(String email) {
        return email.trim();
    }
}
