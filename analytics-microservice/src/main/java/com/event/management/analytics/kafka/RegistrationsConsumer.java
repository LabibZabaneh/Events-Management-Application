package com.event.management.analytics.kafka;

import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.Topic;

@KafkaListener
public class RegistrationsConsumer {

    final String USER_REGISTERED_TOPIC = "user-registered";
    final String USER_UNREGISTERED_TOPIC = "user-unregistered";

    @Topic(USER_REGISTERED_TOPIC)
    public void registerUser(@KafkaKey Long userId, Long eventId){
        System.out.println("User with id " + userId + " registered to event with id " + eventId);
    }

    @Topic(USER_UNREGISTERED_TOPIC)
    public void unregisterUser(@KafkaKey Long userId, Long eventId){
        System.out.println("User with id " + userId + " unregistered to event with id " + eventId);
    }
}
