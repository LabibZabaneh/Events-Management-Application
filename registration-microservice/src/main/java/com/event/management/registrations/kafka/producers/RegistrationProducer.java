package com.event.management.registrations.kafka.producers;

import io.micronaut.configuration.kafka.annotation.KafkaClient;
import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.Topic;

@KafkaClient
public interface RegistrationProducer {

    String USER_REGISTERED_TOPIC = "user-registered";
    String USER_UNREGISTERED_TOPIC = "user-unregistered";

    @Topic(USER_REGISTERED_TOPIC)
    void registeredUser(@KafkaKey Long userId, Long eventId);

    @Topic(USER_UNREGISTERED_TOPIC)
    void unregisteredUser(@KafkaKey Long userId, Long eventId);
}
