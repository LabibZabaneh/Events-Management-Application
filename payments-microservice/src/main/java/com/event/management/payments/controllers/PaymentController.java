package com.event.management.payments.controllers;

import com.event.management.payments.kafka.PaymentsProducer;
import com.event.management.payments.model.PaymentRequest;
import com.event.management.payments.model.PaymentRequestBody;
import com.event.management.payments.model.PaymentResponse;
import com.event.management.payments.services.PaymentService;
import com.stripe.StripeClient;
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

    @Post("/process")
    public HttpResponse<PaymentResponse> processPayment(@Body PaymentRequestBody requestBody) {
        PaymentRequest request = requestBody.getPaymentRequest();
        if (request.getAmount() == null || request.getAmount() <= 0){
            return HttpResponse.status(HttpStatus.BAD_REQUEST, "Invalid payment amount");
        }
        if (request.getPaymentMethod() == null){
            return HttpResponse.status(HttpStatus.BAD_REQUEST, "Invalid payment method");
        }
        if (request.getCurrency() == null){
            return HttpResponse.status(HttpStatus.BAD_REQUEST, "Invalid payment currency");
        }
        if (request.getDescription() == null){
            return HttpResponse.status(HttpStatus.BAD_REQUEST, "Invalid payment description");
        }

        long ticketId = requestBody.getTicketId();
        PaymentResponse response = paymentService.processPayment(request, ticketId);

        if (response.getStatus().equals("success")) {
            return HttpResponse.ok(response);
        } else {
            // Handle payment failure
            return HttpResponse.status(HttpStatus.BAD_REQUEST, response.getMessage());
        }
    }

}
