package com.event.management.registrations.kafka.consumers;

import com.event.management.registrations.domain.Event;
import com.event.management.registrations.domain.TicketCategory;
import com.event.management.registrations.dto.EventDTO;
import com.event.management.registrations.dto.TicketCategoryDTO;
import com.event.management.registrations.repositories.EventsRepository;
import com.event.management.registrations.repositories.TicketCategoriesRepository;
import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.Topic;
import jakarta.inject.Inject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;

@KafkaListener
public class EventsConsumer {

    final String EVENT_POSTED_TOPIC = "event-posted";
    final String EVENT_DELETED_TOPIC = "event-deleted";

    @Inject
    EventsRepository eventsRepo;

    @Inject
    TicketCategoriesRepository ticketCategoriesRepo;

    @Topic(EVENT_POSTED_TOPIC)
    public void postedEvent(@KafkaKey Long id, EventDTO dto){
        Optional<Event> oEvent = eventsRepo.findById(id);
        if (oEvent.isEmpty()){
            Event event = new Event();
            event.setId(id);
            event.setEventName(dto.getEventName());
            event.setRegisteredUsers(new HashSet<>());
            event.setTicketCategories(new ArrayList<>());
            eventsRepo.save(event);

            for (TicketCategoryDTO ticketCategoryDTO : dto.getTicketCategories()){
                TicketCategory ticketCategory = new TicketCategory();
                ticketCategory.setEvent(event);
                ticketCategory.setName(ticketCategoryDTO.getName());
                ticketCategory.setInitialCount(ticketCategoryDTO.getInitialCount());
                ticketCategory.setPrice(ticketCategoryDTO.getPrice());
                ticketCategory.setCurrentCount(0);
                ticketCategoriesRepo.save(ticketCategory);
            }

            eventsRepo.update(event);

            System.out.println("Event added with id" + id);
        }
    }

    @Topic(EVENT_DELETED_TOPIC)
    public void deletedEvent(@KafkaKey Long id, EventDTO dto){
        Optional<Event> oEvent = eventsRepo.findById(id);
        if (oEvent.isPresent()){
            eventsRepo.deleteById(id);

            System.out.println("Event deleted with id" + id);
        }
    }
}
