package com.event.management.notifications.kafka;

import com.event.management.notifications.domain.Event;
import com.event.management.notifications.dto.EventDTO;
import com.event.management.notifications.repositories.EventsRepository;
import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.Topic;
import jakarta.inject.Inject;

import java.util.Optional;


@KafkaListener
public class EventsConsumer {

    final String EVENT_POSTED_TOPIC = "event-posted";
    final String EVENT_DELETED_TOPIC = "event-deleted";

    @Inject
    EventsRepository repo;

    @Topic(EVENT_POSTED_TOPIC)
    public void postedEvent(@KafkaKey Long id, EventDTO dto){
        if (!repo.existsById(id)){
            Event event = new Event();
            event.setId(id);
            event.setName(dto.getName());
            event.setVenue(dto.getVenue());
            event.setTime(dto.getTime());
            event.setDate(dto.getDate());
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
