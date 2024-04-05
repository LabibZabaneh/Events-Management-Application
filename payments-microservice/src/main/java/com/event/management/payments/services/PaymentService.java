package com.event.management.payments.services;

import com.event.management.payments.model.PaymentRequest;
import com.event.management.payments.model.PaymentResponse;
import com.stripe.exception.StripeException;

public interface PaymentService {

    PaymentResponse processPayment(PaymentRequest request, long ticketId) throws StripeException;
}
