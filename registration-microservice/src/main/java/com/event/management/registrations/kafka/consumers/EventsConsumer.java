package com.event.management.registrations.kafka.consumers;

import com.event.management.registrations.domain.Event;
import com.event.management.registrations.dto.EventDTO;
import com.event.management.registrations.repositories.EventsRepository;
import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.Topic;
import jakarta.inject.Inject;

import java.util.HashSet;
import java.util.Optional;

@KafkaListener
public class EventsConsumer {

    final String EVENT_POSTED_TOPIC = "event-posted";

    @Inject
    EventsRepository eventsRepo;

    @Topic(EVENT_POSTED_TOPIC)
    public void postedEvent(@KafkaKey Long id, EventDTO dto){
        Optional<Event> oEvent = eventsRepo.findById(id);
        if (oEvent.isEmpty()){
            Event event = new Event();
            event.setId(id);
            event.setEventName(dto.getEventName());
            event.setRegisteredUsers(new HashSet<>());
            eventsRepo.save(event);

            System.out.println("Event added with id" + id);
        }
    }
}
