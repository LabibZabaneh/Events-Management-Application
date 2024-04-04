package com.event.management.payments.model;

import com.stripe.model.Charge;

public class PaymentResponse {

    private String chargeId;
    private String status;
    private Long amountCaptured;
    private String paymentMethod;

    public PaymentResponse(String chargeId, String status) {
        this.chargeId = chargeId;
        this.status = status;
    }

    public String getChargeId() {
        return chargeId;
    }

    public void setChargeId(String chargeId) {
        this.chargeId = chargeId;
    }

    public String getStatus() {
        return status;
    }

    public Long getAmountCaptured() {
        return amountCaptured;
    }

    public void setAmountCaptured(Long amountCaptured) {
        this.amountCaptured = amountCaptured;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public static PaymentResponse createFromStripeCharge(Charge charge) {
        PaymentResponse response = new PaymentResponse(charge.getId(), charge.getStatus());
        response.setAmountCaptured(charge.getAmountCaptured());
        response.setPaymentMethod(charge.getPaymentMethod());
        return response;
    }
}
