package com.event.management.events.controllers;

import com.event.management.events.domain.Business;
import com.event.management.events.domain.Event;
import com.event.management.events.dto.EventDTO;
import com.event.management.events.repositories.BusinessRepository;
import com.event.management.events.repositories.EventsRepository;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import jakarta.inject.Inject;

import javax.transaction.Transactional;
import java.net.URI;
import java.util.Optional;

@Controller("/events")
public class EventsController {

    @Inject
    EventsRepository repo;

    @Inject
    BusinessRepository businessRepo;

    @Get("/")
    public Iterable<Event> list() {
        return repo.findAll();
    }

    @Transactional
    @Post("/")
    public HttpResponse<Void> addEvent(@Body EventDTO dto) {
        Optional<Business> oBusiness = businessRepo.findById(dto.getBusinessId());
        if (oBusiness.isEmpty()){
            return HttpResponse.notFound();
        }
        Business b = oBusiness.get();
        Event e = new Event();
        e.setEventName(dto.getEventName());
        e.setBusiness(b);
        e.setDate(dto.getDate());
        e.setTime(dto.getTime());
        e.setVenue(dto.getVenue());
        repo.save(e);

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
        repo.delete(oEvent.get());
        return HttpResponse.ok();
    }
}
