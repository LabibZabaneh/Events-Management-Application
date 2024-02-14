package com.event.management.events.controllers;

import com.event.management.events.domain.Event;
import com.event.management.events.repositories.EventsRepository;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import jakarta.inject.Inject;

@Controller
public class EventsController {

    @Inject
    EventsRepository repo;

    @Get("/")
    public Iterable<Event> list() {
        return repo.findAll();
    }

    @Get("/{id}")
    public Event getEvent(long id) {
        return repo.findById(id).orElse(null);
    }
}
