package com.event.management.events.kafka;

import com.event.management.events.dto.BusinessDTO;
import io.micronaut.configuration.kafka.annotation.KafkaClient;
import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.Topic;

@KafkaClient
public interface BusinessProducer {

    String BUSINESS_CREATED_TOPIC = "business-created";
    String BUSINESS_DELETED_TOPIC = "business-deleted";

    @Topic(BUSINESS_CREATED_TOPIC)
    void createdBusiness(@KafkaKey Long id, BusinessDTO dto);

    @Topic(BUSINESS_DELETED_TOPIC)
    void deletedBusiness(@KafkaKey Long id, BusinessDTO dto);
}
