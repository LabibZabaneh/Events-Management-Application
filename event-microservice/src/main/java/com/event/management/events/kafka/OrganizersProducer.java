package com.event.management.events.kafka;

import com.event.management.events.dto.OrganizerDTO;
import io.micronaut.configuration.kafka.annotation.KafkaClient;
import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.Topic;

@KafkaClient
public interface OrganizersProducer {

    String ORGANIZER_CREATED_TOPIC = "organizer-created";
    String ORGANIZER_DELETED_TOPIC = "organizer-deleted";

    @Topic(ORGANIZER_CREATED_TOPIC)
    void createdOrganizer(@KafkaKey Long id, OrganizerDTO dto);

    @Topic(ORGANIZER_DELETED_TOPIC)
    void deletedOrganizer(@KafkaKey Long id, OrganizerDTO dto);
}
