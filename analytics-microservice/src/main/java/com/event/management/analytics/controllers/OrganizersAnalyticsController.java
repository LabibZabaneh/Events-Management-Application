package com.event.management.analytics.controllers;

import com.event.management.analytics.domain.*;
import com.event.management.analytics.repositories.OrganizersRepository;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import jakarta.inject.Inject;

import javax.transaction.Transactional;
import java.util.*;

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

    @Get("/organizers/{id}/registrations-age-distribution")
    public Map<Integer, Integer> getOrganizerRegistrationsAgeDistribution(long id){
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

    @Get("/organizers/{id}/registrations-average-age")
    public double getOrganizerRegistrationsAverageAge(long id){
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

    @Get("/organizers/{id}/followers-age-distribution")
    public List<OrganizerAgeCount> getOrganizerFollowersAgeDistribution(long id){
        Optional<Organizer> oOrganizer = organizersRepo.findById(id);
        if (oOrganizer.isEmpty()){
            return new ArrayList<>();
        }
        return oOrganizer.get().getAgeCounts();
    }

    @Get("/organizers/{id}/followers-average-age")
    public double getOrganizerFollowersAverageAge(long id){
        Optional<Organizer> oOrganizer = organizersRepo.findById(id);
        if (oOrganizer.isEmpty()){
            return 0.0;
        }
        return oOrganizer.get().getAverageAge();
    }

    @Get("/organizers/{id}/followers-gender-distribution")
    public Map<Gender, Integer> getOrganizerFollowersGenderDistribution(long id){
        Optional<Organizer> oOrganizer = organizersRepo.findById(id);
        if (oOrganizer.isEmpty()){
            return Collections.emptyMap();
        }

        Organizer organizer = oOrganizer.get();
        Map<Gender, Integer> genderDistribution = new HashMap<>();
        genderDistribution.put(Gender.MALE, organizer.getMaleFollowers());
        genderDistribution.put(Gender.FEMALE, organizer.getFemaleFollowers());
        genderDistribution.put(Gender.OTHER, organizer.getOtherFollowers());

        return genderDistribution;
    }

    @Get("/organizer/{id}/followers-gender-ratio")
    public Map<Gender, Double> getOrganizerFollowersGenderRatio(long id){
        Optional<Organizer> oOrganizer = organizersRepo.findById(id);
        if (oOrganizer.isEmpty()){
            return Collections.emptyMap();
        }

        Organizer organizer = oOrganizer.get();
        Map<Gender, Double> genderRatio= new HashMap<>();
        int totalFollowers = organizer.getFollowers();

        genderRatio.put(Gender.MALE, (double) (organizer.getMaleFollowers()/totalFollowers)*100);
        genderRatio.put(Gender.FEMALE, (double) (organizer.getFemaleFollowers()/totalFollowers)*100);
        genderRatio.put(Gender.OTHER, (double) (organizer.getOtherFollowers()/totalFollowers)*100);

        return genderRatio;
    }

}
