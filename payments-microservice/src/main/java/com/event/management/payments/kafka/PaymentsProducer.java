package com.event.management.payments.kafka;

import io.micronaut.configuration.kafka.annotation.KafkaClient;
import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.Topic;

@KafkaClient
public interface PaymentsProducer {

    String PAYMENT_SUCCESSFUL_TOPIC = "payment-successful";

    @Topic(PAYMENT_SUCCESSFUL_TOPIC)
    void successfulPayment(@KafkaKey Long ticketId, String status);

}
