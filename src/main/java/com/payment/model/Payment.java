package com.payment.model;




import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String accessToken;
    @Column(columnDefinition = "TEXT")
    private String product;
    @Column(columnDefinition = "TEXT")
    private String clientMssidn;
    @Column(columnDefinition = "TEXT")
    private String refPaiement;
    @Column(columnDefinition = "TEXT")
    private String amount;
    @Column(columnDefinition = "TEXT")
    private String correlationID;

    private LocalDateTime requestDate;
    private Boolean callbackReceived;
    private LocalDateTime callbackDate;

    public Payment() {

    }

    public Payment(Long id, String accessToken, String product, String clientMssidn, String refPaiement, String amount, String correlationID, LocalDateTime requestDate, Boolean callbackReceived, LocalDateTime callbackDate) {
        this.id = id;
        this.accessToken = accessToken;
        this.product = product;
        this.clientMssidn = clientMssidn;
        this.refPaiement = refPaiement;
        this.amount = amount;
        this.correlationID = correlationID;
        this.requestDate = requestDate;
        this.callbackReceived = callbackReceived;
        this.callbackDate = callbackDate;

    }


    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getClientMssidn() {
        return clientMssidn;
    }

    public void setClientMssidn(String clientMssidn) {
        this.clientMssidn = clientMssidn;
    }

    public String getRefPaiement() {
        return refPaiement;
    }

    public void setRefPaiement(String refPaiement) {
        this.refPaiement = refPaiement;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public LocalDateTime getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(LocalDateTime requestDate) {
        this.requestDate = requestDate;
    }

    public Boolean getCallbackReceived() {
        return callbackReceived;
    }

    public void setCallbackReceived(Boolean callbackReceived) {
        this.callbackReceived = callbackReceived;
    }

    public LocalDateTime getCallbackDate() {
        return callbackDate;
    }

    public void setCallbackDate(LocalDateTime callbackDate) {
        this.callbackDate = callbackDate;
    }
    public String getCorrelationID() {
        return correlationID;
    }

    public void setCorrelationID(String correlationID) {
        this.correlationID = correlationID;
    }

    // Getters and Setters
}
