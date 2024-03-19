package com.event.management.analytics;

import com.event.management.analytics.domain.Event;
import com.event.management.analytics.domain.EventAgeCount;
import com.event.management.analytics.domain.Organizer;
import com.event.management.analytics.repositories.EventsRepository;
import com.event.management.analytics.repositories.OrganizersRepository;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest(transactional = false)
public class OrganizersAnalyticsControllerTest {

    @Inject
    OrgainzersAnalyticsClient client;

    @Inject
    OrganizersRepository organizersRepo;

    @Inject
    EventsRepository eventsRepo;

    @BeforeEach
    public void clean(){
        organizersRepo.deleteAll();
    }

    @Test
    public void noOrganizers(){
        assertFalse(client.getOrganizers().iterator().hasNext(), "Service should not have any organizers listed");
    }

    @Test
    public void oneOrganizer(){
        Organizer organizer = createOrganizer(1L, "York Parties", 0);
        organizersRepo.save(organizer);

        List<Organizer> organizers = iterableToList(client.getOrganizers());
        assertEquals(1, organizers.size(), "There should be one organizer");
        assertEquals(organizer.getId(), organizers.get(0).getId(), "The ids should be the same");
    }

    @Test
    public void noPopularOrganizers(){
        assertFalse(client.getPopularOrganizers().isEmpty());
    }

    @Test
    public void ninePopularOrganizer(){
        for (long i=1L;i<=9;i++){
            Organizer organizer = createOrganizer(i, "York Parties" + i, (int) i);
            organizersRepo.save(organizer);
        }
        List<Organizer> organizers = client.getPopularOrganizers();
        assertEquals(9, organizers.size(), "There should be 9 organizers");

        for (int i = 0; i < organizers.size() - 1; i++) {
            Organizer current = organizers.get(i);
            Organizer next = organizers.get(i + 1);
            assertTrue(current.getFollowers() >= next.getFollowers(), "Organizers should be ordered by followers (descending)");
        }
    }

    @Test
    public void tenPopularOrganizers(){
        for (long i=1L;i<=10;i++){
            Organizer organizer = createOrganizer(i, "York Parties" + i, (int) i);
            organizersRepo.save(organizer);
        }

        List<Organizer> organizers = client.getPopularOrganizers();
        assertEquals(10, organizers.size(), "There should be 10 organizers");

        for (int i = 0; i < organizers.size() - 1; i++) {
            Organizer current = organizers.get(i);
            Organizer next = organizers.get(i + 1);
            assertTrue(current.getFollowers() >= next.getFollowers(), "Organizers should be ordered by followers (descending)");
        }
    }

    @Test
    public void elevenPopularOrganizers(){
        for (long i=1L;i<=11;i++){
            Organizer organizer = createOrganizer(i, "York Parties" + i, (int) i);
            organizersRepo.save(organizer);
        }
        List<Organizer> organizers = client.getPopularOrganizers();
        assertEquals(10, organizers.size(), "There should be 10 organizers");

        for (int i = 0; i < organizers.size() - 1; i++) {
            Organizer current = organizers.get(i);
            Organizer next = organizers.get(i + 1);
            assertTrue(current.getFollowers() >= next.getFollowers(), "Organizers should be ordered by followers (descending)");
        }
    }

    @Test
    public void invalidOrganizerPopularEvents(){
        List<Event> organizerPopularEvents = client.getOrganizerPopularEvents(0L);
        assertTrue(organizerPopularEvents.isEmpty(), "Should return an empty list on an invalid organizer");
    }

    @Test
    public void noOrganizerPopularEvents(){
        Organizer organizer = createOrganizer(1L, "York Parties", 0);
        organizersRepo.save(organizer);

        List<Event> organizerPopularEvents = client.getOrganizerPopularEvents(organizer.getId());
        assertTrue(organizerPopularEvents.isEmpty(), "Should return an empty list");
   }

    @Test
    public void organizerSortedPopularEvents(){

    }

    @Test
    public void invalidOrganizerRegistrationsAgeDistribution(){
        Map<Integer, Integer> ageDistribution = client.getOrganizerRegistrationsAgeDistribution(0L);
        assertTrue(ageDistribution.isEmpty(), "Should return an empty age distribution on an invalid organizer");
     }

    @Test
    public void noOrganizerRegistrationsAgeDistribution(){
        Organizer organizer = createOrganizer(1L, "York Parties", 0);
        organizersRepo.save(organizer);

        Map<Integer, Integer> ageDistribution = client.getOrganizerRegistrationsAgeDistribution(organizer.getId());
        assertTrue(ageDistribution.isEmpty(), "Should return an empty age distribution");
    }

    @Test
    public void organizerRegistrationsAgeDistribution(){
        Organizer organizer = createOrganizer(1L, "York Parties", 0);
        organizersRepo.save(organizer);

        final int age1 = 20;
        Event event = createEvent(1L, "Freshers", organizer);
        event.addRegistration(age1);
        eventsRepo.save(event);

        final int age2 = 22;
        Event event2 = createEvent(2L, "Freshers 2", organizer);
        event.addRegistration(age2);
        eventsRepo.save(event);

        Event event3 = createEvent(3L, "Freshers 3", organizer);
        event.addRegistration(age2);
        eventsRepo.save(event);

        organizer.getPostedEvents().add(event);
        organizer.getPostedEvents().add(event2);
        organizer.getPostedEvents().add(event3);
        organizersRepo.update(organizer);

        Map<Integer, Integer> ageDistribution = client.getOrganizerRegistrationsAgeDistribution(organizer.getId());
        assertEquals(2, ageDistribution.size(), "There should be 2 age groups");
        assertEquals(1, ageDistribution.get(age1), "There should be a value of 1 for age " + age1);
        assertEquals(2, ageDistribution.get(age2), "There should be a value of 2 for age " + age2);
    }

    @Test
    public void invalidOrganizerRegistrationsAverageAge(){
        double averageAge = client.getOrganizerRegistrationsAverageAge(0L);
        assertEquals(0.0, averageAge, "Should return 0.0 on an invalid organizer");
    }

    @Test
    public void noOrganizerRegistrationsAverageAge(){
        Organizer organizer = createOrganizer(1L, "York Parties", 0);
        organizersRepo.save(organizer);

        double averageAge = client.getOrganizerRegistrationsAverageAge(organizer.getId());
        assertEquals(0.0, averageAge, "Should return 0.0 for an organizer with no posted events");
    }

    @Test
    public void organizerRegistrationsAverageAge(){
        Organizer organizer = createOrganizer(1L, "York Parties", 0);
        organizersRepo.save(organizer);

        final int age1 = 19;
        Event event = createEvent(1L, "Freshers", organizer);
        event.addRegistration(age1);
        eventsRepo.save(event);

        final int age2 = 22;
        Event event2 = createEvent(2L, "Freshers 2", organizer);
        event.addRegistration(age2);
        eventsRepo.save(event);

        Event event3 = createEvent(3L, "Freshers 3", organizer);
        event.addRegistration(age2);
        eventsRepo.save(event);

        organizer.getPostedEvents().add(event);
        organizer.getPostedEvents().add(event2);
        organizer.getPostedEvents().add(event3);
        organizersRepo.update(organizer);

        double expectedAverageAge = (age1 + 2 * age2)/3;
        double averageAge = client.getOrganizerRegistrationsAverageAge(organizer.getId());
        assertEquals(expectedAverageAge, averageAge, "Average age should be calculated correctly");
    }

    @Test
    public void invalidOrganizerFollowersAgeDistribution(){

    }

    @Test
    public void noOrganizerFollowersAgeDistribution(){

    }

    @Test
    public void organizerFollowersAgeDistribution(){

    }

    @Test
    public void invalidOrganizerFollowersAverageAge(){

    }

    @Test
    public void noOrganizerFollowersAverageAge(){

    }

    @Test
    public void organizerFollowersAverageAge(){

    }

    @Test
    public void invalidOrganizerFollowersTopAgeGroups(){

    }

    @Test
    public void noOrganizerFollowersTopAgeGroups(){

    }

    @Test
    public void organizerFollowersTopAgeGroups(){

    }

    protected static <T> List<T> iterableToList(Iterable<T> iterable) {
        List<T> l = new ArrayList<>();
        iterable.forEach(l::add);
        return l;
    }

    private Organizer createOrganizer(Long id, String name, int followers){
        Organizer organizer = new Organizer();
        organizer.setId(id);
        organizer.setName(name);
        organizer.setFollowers(followers);
        organizer.setAverageAge(0.0);
        organizer.setPostedEvents(new HashSet<>());
        organizer.setAgeCounts(new ArrayList<>());
        return organizer;
    }

    private Event createEvent(Long id, String name, Organizer organizer){
        Event event = new Event();
        event.setId(id);
        event.setName(name);
        event.setOrganizer(organizer);
        event.setRegistrations(0);
        event.setAverageAge(0);
        event.setAgeCounts(new ArrayList<>());
        return event;
    }

}
