package com.event.management.events.controllers;

import com.event.management.events.domain.Event;
import com.event.management.events.domain.Organizer;
import com.event.management.events.domain.TicketCategory;
import com.event.management.events.dto.EventDTO;
import com.event.management.events.dto.TicketCategoryDTO;
import com.event.management.events.kafka.EventsProducer;
import com.event.management.events.repositories.EventsRepository;
import com.event.management.events.repositories.OrganizersRepository;
import com.event.management.events.repositories.TicketCategoriesRepository;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import jakarta.inject.Inject;

import javax.transaction.Transactional;
import java.net.URI;
import java.util.ArrayList;
import java.util.Optional;

@Controller("/events")
public class EventsController {

    @Inject
    EventsRepository repo;

    @Inject
    OrganizersRepository organizersRepo;

    @Inject
    TicketCategoriesRepository ticketCategoriesRepo;

    @Inject
    EventsProducer producer;

    @Get("/")
    public Iterable<Event> list() {
        return repo.findAll();
    }

    @Transactional
    @Post("/")
    public HttpResponse<Void> addEvent(@Body EventDTO dto) {
        Optional<Organizer> oOrganizer = organizersRepo.findById(dto.getOrganizerId());
        if (oOrganizer.isEmpty()){
            return HttpResponse.notFound();
        }
        Organizer o = oOrganizer.get();
        Event e = new Event();
        e.setEventName(dto.getEventName());
        e.setOrganizer(o);
        e.setDate(dto.getDate());
        e.setTime(dto.getTime());
        e.setVenue(dto.getVenue());
        e.setTicketCategories(new ArrayList<>());
        repo.save(e);

        for (TicketCategoryDTO ticketCategoryDTO : dto.getTicketCategories()){
            TicketCategory ticketCategory = new TicketCategory();
            ticketCategory.setName(ticketCategoryDTO.getName());
            ticketCategory.setEvent(e);
            ticketCategory.setInitialCount(ticketCategoryDTO.getInitialCount());
            ticketCategory.setPrice(ticketCategoryDTO.getPrice());
            ticketCategoriesRepo.save(ticketCategory);
        }

        repo.update(e);

        producer.postedEvent(e.getId(), dto);

        return HttpResponse.created(URI.create("/events/" + e.getId()));
    }

    @Get("/{id}")
    public Event getEvent(long id) {
        return repo.findById(id).orElse(null);
    }

    @Transactional
    @Put("/{id}")
    public HttpResponse<Void> updateEvent(long id, @Body EventDTO dto) {
        Optional<Event> oEvent = repo.findById(id);
        if (oEvent.isEmpty()) {
            return HttpResponse.notFound();
        }
        Event e = oEvent.get();
        if (dto.getEventName() != null) {
            e.setEventName(dto.getEventName());
        }
        if (dto.getDate() != null) {
            e.setDate(dto.getDate());
        }
        if (dto.getTime() != null) {
            e.setTime(dto.getTime());
        }
        if (dto.getVenue() != null) {
            e.setVenue(dto.getVenue());
        }
        repo.update(e);
        return HttpResponse.ok();
    }

    @Transactional
    @Delete("/{id}")
    public HttpResponse<Void> deleteEvent(long id) {
        Optional<Event> oEvent = repo.findById(id);
        if (oEvent.isEmpty()) {
            return HttpResponse.notFound();
        }
        Event e = oEvent.get();
        repo.delete(e);

        EventDTO dto = new EventDTO();
        dto.setTime(e.getTime());
        dto.setDate(e.getDate());
        dto.setEventName(e.getEventName());
        dto.setVenue(e.getVenue());
        dto.setOrganizerId(e.getOrganizer().getId());
        producer.deletedEvent(id, dto);

        return HttpResponse.ok();
    }
}
