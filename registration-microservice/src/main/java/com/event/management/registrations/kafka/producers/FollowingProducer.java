package com.event.management.registrations.kafka.producers;

import io.micronaut.configuration.kafka.annotation.KafkaClient;
import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.Topic;

@KafkaClient
public interface FollowingProducer {

    String ORGANIZER_FOLLOWED_TOPIC = "organizer-followed";
    String ORGANIZER_UNFOLLOWED_TOPIC = "organizer-unfollowed";

    @Topic(ORGANIZER_FOLLOWED_TOPIC)
    void followedOrganizer(@KafkaKey Long organizerId, Long userId);

    @Topic(ORGANIZER_UNFOLLOWED_TOPIC)
    void unfollowedOrganizer(@KafkaKey Long organizerId, Long userId);

}
