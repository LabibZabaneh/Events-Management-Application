package com.event.management.registrations.clients;

import com.event.management.registrations.domain.Event;
import com.event.management.registrations.domain.User;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Put;
import io.micronaut.http.client.annotation.Client;

import java.util.Set;

@Client("/registrations")
public interface RegistrationsClient {

    @Get("/users/{id}")
    Set<Event> getUserRegistrations(long id);

    @Get("/events/{id}")
    Set<User> getEventRegistrations(long id);

    @Put("/users/{eventId}/{userId}")
    HttpResponse<Void> addRegistration(long eventId, long userId);

    @Delete("/users/{eventId}/{userId}")
    HttpResponse<Void> deleteRegistration(long eventId, long userId);
}
