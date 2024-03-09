package com.event.management.registrations.kafka.producers;

import io.micronaut.configuration.kafka.annotation.KafkaClient;
import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.Topic;

@KafkaClient
public interface RegistrationProducer {

    String REGISTRATIONS_TOPIC = "registrations";
    String UNREGISTRATIONS_TOPIC = "unregistrations";

    @Topic(REGISTRATIONS_TOPIC)
    void addedRegistration(@KafkaKey Long userId, Long eventId);

    @Topic(UNREGISTRATIONS_TOPIC)
    void addedUnRegistration(@KafkaKey Long userId, Long eventId);
}
