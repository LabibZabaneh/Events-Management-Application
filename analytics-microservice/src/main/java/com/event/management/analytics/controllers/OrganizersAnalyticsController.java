package com.event.management.analytics.controllers;

import com.event.management.analytics.domain.Event;
import com.event.management.analytics.domain.EventAgeCount;
import com.event.management.analytics.domain.Organizer;
import com.event.management.analytics.repositories.OrganizersRepository;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import jakarta.inject.Inject;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller("/analytics")
public class OrganizersAnalyticsController {

    @Inject
    OrganizersRepository organizersRepo;

    @Get("/organizers")
    public Iterable<Organizer> getOrganizers(){
        return organizersRepo.findAll();
    }

    @Get("/organizers/popular")
    public List<Organizer> getPopularOrganizers(){
        return organizersRepo.findTop10ByOrderByFollowersDesc();
    }

    @Transactional
    @Get("/organizers/{id}/popular-events")
    public List<Event> getOrganizerPopularEvents(long id){
        Optional<Organizer> oOrganizer = organizersRepo.findById(id);
        if (oOrganizer.isEmpty()){
            return null;
        }
        return organizersRepo.findTopRegisteredEventsByOrganizerId(id, 10);
    }

    @Get("/organizers/{id}/overall-events-registrations-age-distribution")
    public Map<Integer, Integer> getOrganizerAgeDistribution(long id){
        Optional<Organizer> oOrganizer = organizersRepo.findById(id);
        if (oOrganizer.isEmpty()){
            return null;
        }
        Organizer organizer = oOrganizer.get();
        Map<Integer, Integer> ageCounts = new HashMap<>();
        for (Event event : organizer.getPostedEvents()){
            for (EventAgeCount ageCount : event.getAgeCounts()){
                int age = ageCount.getAge();
                int currentCount = ageCounts.getOrDefault(age, 0); // Get existing count or default to 0
                ageCounts.put(age, currentCount + ageCount.getCount()); // Update count
            }
        }
        return ageCounts;
    }

    @Get("/organizers/{id}/overall-events-registrations-average-age")
    public double getOrganizerAverageAge(long id){
        Optional<Organizer> oOrganizer = organizersRepo.findById(id);
        if (oOrganizer.isEmpty()){
            return 0.0;
        }
        Organizer organizer = oOrganizer.get();
        double totalRegistrations = 0.0;
        double totalAge = 0.0;
        for (Event event : organizer.getPostedEvents()){
            totalRegistrations += (double) event.getRegistrations();
            totalAge += event.getAverageAge() * (double) event.getRegistrations();
        }
        if (totalRegistrations == 0.0){
            return 0.0;
        }
        return totalAge/totalRegistrations;
    }
}
