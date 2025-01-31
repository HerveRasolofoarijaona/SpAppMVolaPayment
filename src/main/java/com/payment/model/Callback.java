package com.payment.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Callback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Long id;

    private String paymentId;
    @Column(columnDefinition = "TEXT")
    private String callbackData;
    private LocalDateTime receivedAt;

    // Constructors, Getters, Setters

    public Callback() {}

    public Callback(String paymentId, String callbackData, LocalDateTime receivedAt) {
        this.paymentId = paymentId;
        this.callbackData = callbackData;
        this.receivedAt = receivedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getCallbackData() {
        return callbackData;
    }

    public void setCallbackData(String callbackData) {
        this.callbackData = callbackData;
    }

    public LocalDateTime getReceivedAt() {
        return receivedAt;
    }

    public void setReceivedAt(LocalDateTime receivedAt) {
        this.receivedAt = receivedAt;
    }
}
