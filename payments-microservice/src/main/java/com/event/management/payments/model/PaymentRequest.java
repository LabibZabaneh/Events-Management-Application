package com.event.management.payments.model;

public class PaymentRequest {

    private Integer amount;
    private String currency;
    private String description;
    private String paymentMethodId;

    public PaymentRequest(Integer amount, String currency, String description, String paymentMethodId) {
        this.amount = amount;
        this.currency = currency;
        this.description = description;
        this.paymentMethodId = paymentMethodId;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPaymentMethodId() {
        return paymentMethodId;
    }

    public void setPaymentMethodId(String paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }
}
