package com.event.management.payments.model;

import com.stripe.model.Charge;

public class PaymentResponse {

    private String chargeId;
    private String status;
    private String message;

    public PaymentResponse(String chargeId, String status, String message) {
        this.chargeId = chargeId;
        this.status = status;
        this.message = message;
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

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
