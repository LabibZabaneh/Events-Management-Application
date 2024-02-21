package com.event.management.events.kafka;

import com.event.management.events.dto.UserDTO;
import io.micronaut.configuration.kafka.annotation.KafkaClient;
import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.Topic;

@KafkaClient
public interface UsersProducer {

    String USER_CREATED_TOPIC = "user-created";
    String USER_DELETED_TOPIC = "user-deleted";

    @Topic(USER_CREATED_TOPIC)
    void createdUser(@KafkaKey Long id, UserDTO dto);

    @Topic(USER_DELETED_TOPIC)
    void deletedUser(@KafkaKey Long id, UserDTO dto);

}
