package com.event.management.payments.controllers;

import com.event.management.payments.kafka.PaymentsProducer;
import com.event.management.payments.model.PaymentRequest;
import com.event.management.payments.model.PaymentRequestBody;
import com.event.management.payments.model.PaymentResponse;
import com.event.management.payments.services.PaymentService;
import com.stripe.StripeClient;
import com.stripe.exception.StripeException;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import jakarta.inject.Inject;

@Controller("/payments")
public class PaymentController {

    @Inject
    private PaymentService paymentService;

    @Inject
    private PaymentsProducer producer;

    @Post("/process")
    public HttpResponse<PaymentResponse> processPayment(@Body PaymentRequestBody requestBody) {
        PaymentRequest request = requestBody.getPaymentRequest();

        // Validate payment request
        HttpResponse<PaymentResponse> validationResponse = validatePaymentRequest(request);
        if (validationResponse != null) {
            return validationResponse;
        }

        long ticketId = requestBody.getTicketId();
        try {
            PaymentResponse response = paymentService.processPayment(request, ticketId);
            return HttpResponse.ok(response);
        } catch (StripeException e) {
            e.printStackTrace();
            producer.unsuccessfulPayment(ticketId, "Stripe API error");
            return HttpResponse.status(HttpStatus.INTERNAL_SERVER_ERROR, "Stripe API error: " + e.getMessage());
        } catch (RuntimeException e) {
            e.printStackTrace();
            producer.unsuccessfulPayment(ticketId, "Failed payment");
            return HttpResponse.status(HttpStatus.BAD_REQUEST, "Payment failed: " + e.getMessage());
        }
    }

    private HttpResponse<PaymentResponse> validatePaymentRequest(PaymentRequest request){
        if (request.getAmount() == null || request.getAmount() <= 0) {
            return HttpResponse.status(HttpStatus.BAD_REQUEST, "Invalid payment amount");
        }
        if (request.getPaymentMethod() == null) {
            return HttpResponse.status(HttpStatus.BAD_REQUEST, "Invalid payment method");
        }
        if (request.getCurrency() == null) {
            return HttpResponse.status(HttpStatus.BAD_REQUEST, "Invalid payment currency");
        }
        if (request.getDescription() == null) {
            return HttpResponse.status(HttpStatus.BAD_REQUEST, "Invalid payment description");
        }
        return null;
    }

}
