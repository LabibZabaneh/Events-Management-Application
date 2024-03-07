package com.event.management.analytics.controllers;

import com.event.management.analytics.domain.Event;
import com.event.management.analytics.domain.Organizer;
import com.event.management.analytics.repositories.EventsRepository;
import com.event.management.analytics.repositories.OrganizersRepository;
import com.event.management.analytics.repositories.UsersRepository;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import jakarta.inject.Inject;

import java.util.List;

@Controller("/analytics")
public class TrendingController {

    @Inject
    EventsRepository eventsRepo;

    @Inject
    UsersRepository usersRepo;

    @Inject
    OrganizersRepository organizersRepo;

    @Get("/popular/events")
    public List<Event> getPopularEvents(){
        return eventsRepo.findTop10ByOrderByRegistrationsDesc();
    }

    @Get("/trending/events")
    public void getTrendingEvents(){

    }

    @Get("/popular/organizers/")
    public List<Organizer> getPopularOrganizers(){
        return organizersRepo.findTop10ByOrderByFollowersDesc();
    }

    @Get("/trending/organizers/")
    public void getTrendingOrganizers(){

    }

    @Get("/activity/users/")
    public void getActiveUsers(){

    }

}
