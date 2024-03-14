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
import java.util.*;

@Controller("/analytics")
public class EventsAnalyticsController {

    @Inject
    EventsRepository eventsRepo;

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

    @Get("/events/{id}/average-age")
    public double getEventAverageAge(long id){
        Optional<Event> oEvent = eventsRepo.findById(id);
        if (oEvent.isEmpty()){
            return 0.0;
        }
        return oEvent.get().getAverageAge();
    }

    @Get("/events/{id}/top-age-groups/{limit}")
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

}
