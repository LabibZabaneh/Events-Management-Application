package com.event.management.events.clients;

import com.event.management.events.domain.Organizer;
import com.event.management.events.dto.OrganizerDTO;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.http.client.annotation.Client;

@Client("/organizers")
public interface OrganizerClient {

    @Get("/")
    Iterable<Organizer> list();

    @Post("/")
    HttpResponse<Void> addOrganizer(@Body OrganizerDTO dto);

    @Get("/{id}")
    Organizer getOrganizer(long id);

    @Put("/{id}")
    HttpResponse<Void> updateOrganizer(long id, @Body OrganizerDTO dto);

    @Delete("/{id}")
    HttpResponse<Void> deleteOrganizer(long id);
}
