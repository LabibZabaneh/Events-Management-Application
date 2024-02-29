package com.event.management.registrations.kafka.consumers;

import com.event.management.registrations.domain.Event;
import com.event.management.registrations.dto.EventDTO;
import com.event.management.registrations.repositories.EventsRepository;
import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.Topic;
import jakarta.inject.Inject;

import java.util.Optional;

@KafkaListener
public class EventsConsumer {

    @Inject
    EventsRepository eventsRepo;

    @Topic("event-posted")
    public void postedEvent(Long id, EventDTO dto){
        Optional<Event> oEvent = eventsRepo.findById(id);
        if (oEvent.isEmpty()){
            Event event = new Event();
            event.setId(id);
            event.setEventName(dto.getEventName());
            event.setDate(dto.getDate());
            event.setTime(dto.getTime());
            event.setVenue(dto.getVenue());
            eventsRepo.save(event);

            System.out.println("Event added with id" + id);
        }
    }
}
