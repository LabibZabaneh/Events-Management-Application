package com.event.management.registrations.clients;

import com.event.management.registrations.domain.Organizer;
import com.event.management.registrations.domain.User;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Put;
import io.micronaut.http.client.annotation.Client;

@Client("/following")
public interface FollowingClient {

    @Get("/organizers/{id}")
    Iterable<User> getOrganizerFollowers(long id);

    @Get("/users/{id}")
    Iterable<Organizer> getUserFollowing(long id);

    @Put("/users/{organizerId}/{userId}")
    HttpResponse<Void> addFollower(long organizerId, long userId);

    @Delete("/users/{organizerId}/{userId}")
    HttpResponse<Void> deleteFollower(long organizerId, long userId);
}
