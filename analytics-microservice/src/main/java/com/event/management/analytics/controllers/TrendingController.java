package com.event.management.analytics.controllers;

import com.event.management.analytics.domain.Event;
import com.event.management.analytics.repositories.EventsRepository;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import jakarta.inject.Inject;

import java.util.List;

@Controller("/analytics")
public class TrendingController {

    @Inject
    EventsRepository eventsRepo;

    @Get("/popular/events")
    public List<Event> getPopularEvents(){
        return eventsRepo.findTop10ByOrderByRegistrationsDesc();
    }

    @Get("/trending/events")
    public void getTrendingEvents(){

    }

    @Get("/popular/organizers/")
    public void getPopularOrganizers(){

    }

    @Get("/trending/organizers/")
    public void getTrendingOrganizers(){

    }

    @Get("/activity/users/")
    public void getActiveUsers(){

    }

}
