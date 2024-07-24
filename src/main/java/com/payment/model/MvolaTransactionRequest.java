package com.payment.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MvolaTransactionRequest {

    private Double amount;
    private String currency;
    private String descriptionText;
    private String requestingOrganisationTransactionReference;
    private String requestDate;
    private String originalTransactionReference;
    private List<Party> debitParty;
    private List<Party> creditParty;
    private List<Metadata> metadata;

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDescriptionText() {
        return descriptionText;
    }

    public void setDescriptionText(String descriptionText) {
        this.descriptionText = descriptionText;
    }

    public String getRequestingOrganisationTransactionReference() {
        return requestingOrganisationTransactionReference;
    }

    public void setRequestingOrganisationTransactionReference(String requestingOrganisationTransactionReference) {
        this.requestingOrganisationTransactionReference = requestingOrganisationTransactionReference;
    }

    public String getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(String requestDate) {
        this.requestDate = requestDate;
    }

    public String getOriginalTransactionReference() {
        return originalTransactionReference;
    }

    public void setOriginalTransactionReference(String originalTransactionReference) {
        this.originalTransactionReference = originalTransactionReference;
    }

    public List<Party> getDebitParty() {
        return debitParty;
    }

    public void setDebitParty(List<Party> debitParty) {
        this.debitParty = debitParty;
    }

    public List<Party> getCreditParty() {
        return creditParty;
    }

    public void setCreditParty(List<Party> creditParty) {
        this.creditParty = creditParty;
    }

    public List<Metadata> getMetadata() {
        return metadata;
    }

    public void setMetadata(List<Metadata> metadata) {
        this.metadata = metadata;
    }

    public MvolaTransactionRequest() {}

    public MvolaTransactionRequest(Double amount, String currency, String descriptionText,
                                   String requestingOrganisationTransactionReference, String requestDate,
                                   String originalTransactionReference, List<Party> debitParty,
                                   List<Party> creditParty, List<Metadata> metadata) {
        this.amount = amount;
        this.currency = currency;
        this.descriptionText = descriptionText;
        this.requestingOrganisationTransactionReference = requestingOrganisationTransactionReference;
        this.requestDate = requestDate;
        this.originalTransactionReference = originalTransactionReference;
        this.debitParty = debitParty;
        this.creditParty = creditParty;
        this.metadata = metadata;
    }


    public static class Party {
        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        private String value;
        private String key;

    }

    public static class Metadata {
        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        private String value;
        private String key;

    }

    public String toJSON() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            // Log the exception details or throw a more specific exception
            e.printStackTrace();
            throw new RuntimeException("Error converting object to JSON", e);
        }
    }
}
