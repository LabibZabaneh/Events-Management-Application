package com.event.management.payments.services;

import com.event.management.payments.kafka.PaymentsProducer;
import com.event.management.payments.model.PaymentRequest;
import com.event.management.payments.model.PaymentResponse;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.param.ChargeCreateParams;
import jakarta.inject.Inject;

public class PaymentServiceImpl implements PaymentService {

    @Inject
    private String stripeSecretKey;

    @Inject
    PaymentsProducer producer;

    @Override
    public PaymentResponse processPayment(PaymentRequest paymentRequest, long ticketId) {
        Stripe.apiKey = stripeSecretKey;

        try {
            // Create a charge with Stripe
            ChargeCreateParams.Builder builder = ChargeCreateParams.builder()
                    .setAmount(paymentRequest.getAmount())
                    .setCurrency(paymentRequest.getCurrency())
                    .setDescription(paymentRequest.getDescription())
                    .setSource(paymentRequest.getPaymentMethod());

            Charge charge = Charge.create(builder.build());

            // Handle successful payment
            if (charge.getPaid()) {
                // Mark ticket as paid
                producer.successfulPayment(ticketId, "paid");
                // Return payment response
                return new PaymentResponse(charge.getId(), charge.getStatus(), "Payment successful");
            } else {
                // Return failed payment response
                return new PaymentResponse(null, "failed", "Payment failed");
            }
        } catch (StripeException e) {
            e.printStackTrace();
            return new PaymentResponse(null, "failed", "Payment failed");
        }
    }
}
