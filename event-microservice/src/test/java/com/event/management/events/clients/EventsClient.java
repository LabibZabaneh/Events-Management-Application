package com.event.management.events.clients;

import com.event.management.events.domain.Event;
import com.event.management.events.dto.EventDTO;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.http.client.annotation.Client;

@Client("/events")
public interface EventsClient {

    @Get("/")
    Iterable<Event> list();

    @Post("/")
    HttpResponse<Void> addEvent(@Body EventDTO dto);

    @Get("/{id}")
    Event getEvent(long id);

    @Put("/{id}")
    HttpResponse<Void> updateEvent(long id, @Body EventDTO dto);

    @Delete("/{id}")
    HttpResponse<Void> deleteEvent(long id);
}
