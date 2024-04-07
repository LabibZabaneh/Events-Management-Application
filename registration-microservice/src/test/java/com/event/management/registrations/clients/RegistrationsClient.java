package com.event.management.registrations.clients;

import com.event.management.registrations.domain.Ticket;
import com.event.management.registrations.domain.TicketCategory;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Put;
import io.micronaut.http.client.annotation.Client;

import java.util.List;
import java.util.Set;

@Client("/registrations")
public interface RegistrationsClient {

    @Get("/users/{id}/tickets")
    Set<Ticket> getUserTickets(long id);

    @Get("/events/{id}/ticket-categories")
    List<TicketCategory> getEventTicketCategories(long id);

    @Put("/users/{userId}/{eventId}/{ticketCategoryId}")
    Long addRegistration(long userId, long eventId, long ticketCategoryId);

    @Delete("/users/{userId}/{eventId}/{ticketCategoryId}")
    HttpResponse<String> deleteRegistration(long eventId, long userId, long ticketCategoryId);
}
