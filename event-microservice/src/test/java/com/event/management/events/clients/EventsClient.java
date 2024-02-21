package com.event.management.events.clients;

import com.event.management.events.domain.Event;
import com.event.management.events.dto.EventDTO;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.http.client.annotation.Client;

@Client("/events")
public interface EventsClient {

    @Get("/")
    public Iterable<Event> list();

    @Post("/")
    public HttpResponse<Void> addEvent(@Body EventDTO dto);

    @Get("/{id}")
    public Event getEvent(long id);

    @Put("/{id}")
    public HttpResponse<Void> updateEvent(long id, @Body EventDTO dto);

    @Delete("/{id}")
    public HttpResponse<Void> deleteEvent(long id);
}
