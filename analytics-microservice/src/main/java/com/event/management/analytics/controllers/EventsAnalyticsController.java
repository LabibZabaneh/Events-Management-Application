package com.event.management.analytics.controllers;

import com.event.management.analytics.domain.EventAgeCount;
import com.event.management.analytics.domain.Event;
import com.event.management.analytics.domain.Gender;
import com.event.management.analytics.repositories.EventsRepository;
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

    @Get("/events/{id}/age-distribution")
    public List<EventAgeCount> getEventAgeDistribution(long id){
        Optional<Event> oEvent = eventsRepo.findById(id);
        if (oEvent.isEmpty()){
            return Collections.emptyList();
        }
        return oEvent.get().getAgeCounts()
                ;
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
    public List<EventAgeCount> getEventTopAgeGroups(long id, int limit){
        Optional<Event> oEvent = eventsRepo.findById(id);
        if (oEvent.isEmpty()){
            return Collections.emptyList();
        }
        List<EventAgeCount> ageCounts = oEvent.get().getAgeCounts();
        ageCounts.sort((a1, a2) -> a2.getCount() - a1.getCount());
        return ageCounts.subList(0, Math.min(limit, ageCounts.size()));
    }

    @Get("/popular/events")
    public List<Event> getPopularEvents(){
        return eventsRepo.findTop10ByOrderByRegistrationsDesc();
    }

    @Get("/events/{id}/gender-distribution")
    public Map<Gender, Integer> getEventGenderDistribution(long id){
        Optional<Event> oEvent = eventsRepo.findById(id);
        if (oEvent.isEmpty()){
            return Collections.emptyMap();
        }
        Event event = oEvent.get();

        Map<Gender, Integer> genderDistribution = new HashMap<>();
        genderDistribution.put(Gender.MALE, event.getMaleRegistrations());
        genderDistribution.put(Gender.FEMALE, event.getFemaleRegistrations());
        genderDistribution.put(Gender.OTHER, event.getOtherRegistrations());

        return genderDistribution;
    }

    @Get("/events/{id}/gender-ratio")
    public Map<Gender, Double> getEventGenderRatios(long id){
        Optional<Event> oEvent = eventsRepo.findById(id);
        if (oEvent.isEmpty()){
            return Collections.emptyMap();
        }

        Event event = oEvent.get();
        int totalRegistrations = event.getRegistrations();
        Map<Gender, Double> genderRatio = new HashMap<>();

        if (totalRegistrations == 0) {
            genderRatio.put(Gender.MALE, 0.0);
            genderRatio.put(Gender.FEMALE, 0.0);
            genderRatio.put(Gender.OTHER, 0.0);
        } else {
            genderRatio.put(Gender.MALE, ((double) event.getMaleRegistrations() / totalRegistrations) * 100);
            genderRatio.put(Gender.FEMALE, ((double) event.getFemaleRegistrations() / totalRegistrations) * 100);
            genderRatio.put(Gender.OTHER, ((double) event.getOtherRegistrations() / totalRegistrations) * 100);
        }

        return genderRatio;
    }

}
