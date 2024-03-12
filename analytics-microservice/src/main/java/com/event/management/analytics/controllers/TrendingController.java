package com.event.management.analytics.controllers;

import com.event.management.analytics.domain.AgeCount;
import com.event.management.analytics.domain.Event;
import com.event.management.analytics.domain.Organizer;
import com.event.management.analytics.repositories.EventsRepository;
import com.event.management.analytics.repositories.OrganizersRepository;
import com.event.management.analytics.repositories.UsersRepository;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import jakarta.inject.Inject;

import javax.transaction.Transactional;
import java.util.Collections;
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

    @Get("/events")
    public Iterable<Event> getEvents(){
        return eventsRepo.findAll();
    }

    @Transactional
    @Get("/events/{id}/age-distribution")
    public List<AgeCount> getEventAgeDistribution(long id){
        Optional<Event> oEvent = eventsRepo.findById(id);
        if (oEvent.isEmpty()){
            return Collections.emptyList();
        }
        return eventsRepo.findById(id).get().getAgeCounts();
    }

    @Transactional
    @Get("/events/{id}/average-age")
    public double getEventAverageAge(long id){
        Optional<Event> oEvent = eventsRepo.findById(id);
        if (oEvent.isEmpty()){
            return 0.0;
        }
        Event event = oEvent.get();
        int totalRegistrations = event.getRegistrations();
        if (totalRegistrations == 0){
            return 0.0;
        }
        int totalAge = 0;
        for (AgeCount ageCount : event.getAgeCounts()){
            totalAge += ageCount.getAge() * ageCount.getCount();
        }
        return (double) totalAge / totalRegistrations;
    }

    @Transactional
    @Get("events/{id}/top-age-groups/{limit}")
    public List<AgeCount> getEventTopAgeGroups(long id, int limit){
        Optional<Event> oEvent = eventsRepo.findById(id);
        if (oEvent.isEmpty()){
            return Collections.emptyList();
        }
        List<AgeCount> ageCounts = oEvent.get().getAgeCounts();
        ageCounts.sort((a1, a2) -> a2.getCount() - a1.getCount());
        return ageCounts.subList(0, Math.min(limit, ageCounts.size()));
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
