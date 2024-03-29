package com.event.management.analytics.controllers;

import com.event.management.analytics.domain.*;
import com.event.management.analytics.repositories.OrganizersRepository;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import jakarta.inject.Inject;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

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

    @Get("/organizers/{id}/followers-age-distribution")
    public Set<AgeCount> getOrganizerFollowersAgeDistribution(long id){
        Optional<Organizer> oOrganizer = organizersRepo.findById(id);
        if (oOrganizer.isEmpty()){
            return Collections.emptySet();
        }
        return oOrganizer.get().getAgeCounts();
    }

    @Get("/organizers/{id}/followers-average-age")
    public double getOrganizerFollowersAverageAge(long id){
        Optional<Organizer> oOrganizer = organizersRepo.findById(id);
        return oOrganizer.map(Organizer::getAverageAge).orElse(0.0);
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

    @Get("/organizers/{id}/registrations-age-distribution")
    public Map<Integer, Integer> getOrganizerRegistrationsAgeDistribution(long id){
        Optional<Organizer> oOrganizer = organizersRepo.findById(id);
        if (oOrganizer.isEmpty()){
            return null;
        }
        Organizer organizer = oOrganizer.get();
        Map<Integer, Integer> ageCounts = new HashMap<>();
        for (Event event : organizer.getPostedEvents()){
            for (AgeCount ageCount : event.getAgeCounts()){
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

    @Get("/organizers/{id}/registrations-gender-distribution")
    public Map<Gender, Integer> getOrganizerRegistrationsGenderDistribution(long id){
        Optional<Organizer> oOrganizer = organizersRepo.findById(id);
        if (oOrganizer.isEmpty()){
            return Collections.emptyMap();
        }
        return getPostedEventsGenderCount(oOrganizer.get().getPostedEvents());
    }

    @Get("/organizers/{id}/registrations-gender-ratio")
    public Map<Gender, Double> getOrganizerRegistrationsGenderRatio(long id){
        Optional<Organizer> oOrganizer = organizersRepo.findById(id);
        if (oOrganizer.isEmpty()){
            return Collections.emptyMap();
        }
        Organizer organizer = oOrganizer.get();
        Map<Gender, Integer> genderCount = getPostedEventsGenderCount(organizer.getPostedEvents());
        int totalGenderCount = genderCount.values().stream().mapToInt(Integer::intValue).sum();
        if (totalGenderCount == 0){
            // convert genderCount's values to double from Integer, they are already set to zero
            return genderCount.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> (double) entry.getValue()));
        }
        Map<Gender, Double> genderRatio = genderCount.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> (double) (entry.getValue() / totalGenderCount)*100
                ));
        return genderRatio;
    }

    private Map<Gender, Integer> getPostedEventsGenderCount(Set<Event> events){
        int totalMaleCount = 0, totalFemaleCount = 0, totalOtherCount = 0;
        for (Event event : events){
            totalMaleCount += event.getMaleRegistrations();
            totalFemaleCount += event.getFemaleRegistrations();
            totalOtherCount += event.getOtherRegistrations();
        }
        Map<Gender, Integer> genderCount = new HashMap<>();
        genderCount.put(Gender.MALE, totalMaleCount);
        genderCount.put(Gender.FEMALE, totalFemaleCount);
        genderCount.put(Gender.OTHER, totalOtherCount);
        return genderCount;
    }
}
