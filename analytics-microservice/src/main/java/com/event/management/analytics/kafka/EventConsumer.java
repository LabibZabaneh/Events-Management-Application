package com.event.management.analytics.kafka;

import com.event.management.analytics.domain.Event;
import com.event.management.analytics.dto.EventDTO;
import com.event.management.analytics.repositories.EventsRepository;
import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.Topic;
import jakarta.inject.Inject;

import java.util.Optional;

@KafkaListener
public class EventConsumer {

    final String EVENT_POSTED_TOPIC = "event-post";
    final String EVENT_DELETED_TOPIC = "event-deleted";

    @Inject
    EventsRepository repo;

    @Topic(EVENT_POSTED_TOPIC)
    public void postedEvent(@KafkaKey Long id, EventDTO dto){
        Optional<Event> oEvent = repo.findById(id);
        if (oEvent.isEmpty()){
            Event event = new Event();
            event.setId(id);
            event.setName(dto.getName());
            event.setRegistrations(0);
            repo.save(event);
            System.out.println("Event posted with id " + id);
        }
    }

    @Topic(EVENT_DELETED_TOPIC)
    public void deletedEvent(@KafkaKey Long id, EventDTO dto){
        Optional<Event> oEvent = repo.findById(id);
        if (oEvent.isPresent()){
            repo.deleteById(id);
            System.out.println("Event deleted with id " + id);
        }
    }
}
