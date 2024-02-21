package com.event.management.events.kafka;

import com.event.management.events.dto.EventDTO;
import io.micronaut.configuration.kafka.annotation.KafkaClient;
import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.Topic;

@KafkaClient
public interface EventsProducer {

    String EVENT_POSTED_TOPIC = "event-post";
    String EVENT_DELETED_TOPIC = "event-deleted";

    @Topic(EVENT_POSTED_TOPIC)
    void postedEvent(@KafkaKey Long id, EventDTO dto);

    @Topic(EVENT_DELETED_TOPIC)
    void deletedEvent(@KafkaKey Long id, EventDTO dto);
}
