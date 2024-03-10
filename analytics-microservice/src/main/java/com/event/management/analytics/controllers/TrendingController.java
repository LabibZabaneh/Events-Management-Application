package com.event.management.analytics.controllers;

import com.event.management.analytics.domain.Event;
import com.event.management.analytics.domain.Organizer;
import com.event.management.analytics.repositories.EventsRepository;
import com.event.management.analytics.repositories.OrganizersRepository;
import com.event.management.analytics.repositories.UsersRepository;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import jakarta.inject.Inject;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Controller("/analytics")
public class TrendingController {

    @Inject
    EventsRepository eventsRepo;

    @Inject
    UsersRepository usersRepo;

    @Inject
    OrganizersRepository organizersRepo;

    @Get("/organizers")
    public Iterable<Organizer> getOrganizers(){
        return organizersRepo.findAll();
    }

    @Transactional
    @Get("organizers/{id}/popular-events")
    public List<Event> getOrganizerPopularEvents(long id){
        Optional<Organizer> oOrganizer = organizersRepo.findById(id);
        if (oOrganizer.isEmpty()){
            return null;
        }
        return organizersRepo.findTopRegisteredEventsByOrganizerId(id, 10);
    }

    @Get("/popular/events")
    public List<Event> getPopularEvents(){
        return eventsRepo.findTop10ByOrderByRegistrationsDesc();
    }

    @Get("/popular/organizers")
    public List<Organizer> getPopularOrganizers(){
        return organizersRepo.findTop10ByOrderByFollowersDesc();
    }



    @Get("/activity/users")
    public void getActiveUsers(){}

}
