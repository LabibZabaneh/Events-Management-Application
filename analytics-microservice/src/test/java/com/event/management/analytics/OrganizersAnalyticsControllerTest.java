package com.event.management.analytics;

import com.event.management.analytics.domain.*;
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

import static com.event.management.analytics.EventsAnalyticsControllerTest.createEvent;
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
        eventsRepo.deleteAll();
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
        event.addRegistration(age1, Gender.MALE);
        eventsRepo.save(event);

        final int age2 = 22;
        Event event2 = createEvent(2L, "Freshers 2", organizer);
        event.addRegistration(age2, Gender.MALE);
        eventsRepo.save(event);

        Event event3 = createEvent(3L, "Freshers 3", organizer);
        event.addRegistration(age2, Gender.MALE);
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
        event.addRegistration(age1, Gender.MALE);
        eventsRepo.save(event);

        final int age2 = 22;
        Event event2 = createEvent(2L, "Freshers 2", organizer);
        event.addRegistration(age2, Gender.MALE);
        eventsRepo.save(event);

        Event event3 = createEvent(3L, "Freshers 3", organizer);
        event.addRegistration(age2, Gender.MALE);
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
        List<OrganizerAgeCount> organizerAgeCounts = client.getOrganizerFollowersAgeDistribution(0L);
        assertTrue(organizerAgeCounts.isEmpty(), "should return an empty list for an invalid organizers");
    }

    @Test
    public void noOrganizerFollowersAgeDistribution(){
        Organizer organizer = createOrganizer(1L, "York Parties", 0);
        organizersRepo.save(organizer);

        List<OrganizerAgeCount> organizerAgeCounts = client.getOrganizerFollowersAgeDistribution(organizer.getId());
        assertTrue(organizerAgeCounts.isEmpty(), "should return an empty list for an invalid organizers");
    }

    @Test
    public void organizerFollowersAgeDistribution(){
        final int age1 = 18;
        final int age2 = 21;
        Organizer organizer = createOrganizer(1L, "York Parties", 0);
        organizer.addFollower(age1, Gender.MALE);
        organizer.addFollower(age1, Gender.MALE);
        organizer.addFollower(age2, Gender.MALE);
        organizersRepo.save(organizer);

        List<OrganizerAgeCount> organizerAgeCounts = client.getOrganizerFollowersAgeDistribution(organizer.getId());
        assertEquals(2, organizerAgeCounts.size(), "Should have 2 age counts");
        assertEquals(age1, organizerAgeCounts.get(0).getAge(), "The first age count should be age1");
        assertEquals(2, organizerAgeCounts.get(0).getCount(), "the first age count should have a count of 2");
        assertEquals(age2, organizerAgeCounts.get(1).getAge(), "The second age count should be age2");
        assertEquals(1, organizerAgeCounts.get(1).getCount(), "the second age count should have a count of 1");
    }

    @Test
    public void invalidOrganizerFollowersAverageAge(){
        double averageAge = client.getOrganizerFollowersAverageAge(0L);
        assertEquals(0.0, averageAge, "Should return 0.0 for an invalid organizer");
    }

    @Test
    public void noOrganizerFollowersAverageAge(){
        Organizer organizer = createOrganizer(1L, "York Parties", 0);
        organizersRepo.save(organizer);

        double averageAge = client.getOrganizerFollowersAverageAge(organizer.getId());
        assertEquals(0.0, averageAge, "Should return 0.0 for organizer with zero followers");
    }

    @Test
    public void organizerFollowersAverageAge(){
        final int age1 = 18;
        final int age2 = 21;
        Organizer organizer = createOrganizer(1L, "York Parties", 0);
        organizer.addFollower(age1, Gender.MALE);
        organizer.addFollower(age1, Gender.MALE);
        organizer.addFollower(age2, Gender.MALE);
        organizersRepo.save(organizer);

        double expectedAverageAge = (age1 * 2 + age2)/3;
        double averageAge = client.getOrganizerFollowersAverageAge(organizer.getId());
        assertEquals(expectedAverageAge, averageAge, "Average age should be calculated correctly");
    }

    @Test
    public void invalidOrganizerFollowersTopAgeGroups(){
        List<OrganizerAgeCount> topAgeGroups = client.getOrganizerFollowersTopAgeGroups(0L, 2);
        assertTrue(topAgeGroups.isEmpty(), "Should return an empty list for an invalid organizer");
    }

    @Test
    public void noOrganizerFollowersTopAgeGroups(){
        Organizer organizer = createOrganizer(1L, "York Parties", 0);
        organizersRepo.save(organizer);

        final int limit = 2;
        List<OrganizerAgeCount> topAgeGroups = client.getOrganizerFollowersTopAgeGroups(organizer.getId(), limit);
        assertEquals(0, topAgeGroups.size(), "Should return an empty list");
    }

    @Test
    public void organizerFollowersTopAgeGroups(){
        final int age1 = 18;
        final int age2 = 21;
        final int age3 = 22;
        Organizer organizer = createOrganizer(1L, "York Parties", 0);
        organizer.addFollower(age1, Gender.MALE);
        organizer.addFollower(age1, Gender.MALE);
        organizer.addFollower(age2, Gender.MALE);
        organizer.addFollower(age3, Gender.MALE);
        organizer.addFollower(age3, Gender.MALE);
        organizer.addFollower(age3, Gender.MALE);
        organizersRepo.save(organizer);

        final int limit = 2;
        List<OrganizerAgeCount> topAgeGroups = client.getOrganizerFollowersTopAgeGroups(organizer.getId(), limit);
        assertEquals(limit, topAgeGroups.size(), "Size should match the limit");
        assertEquals(age3, topAgeGroups.get(0).getAge(), "The first age count should be age3");
        assertEquals(3, topAgeGroups.get(0).getCount(), "Age3 count should be 3");
        assertEquals(age1, topAgeGroups.get(1).getAge(), "The first age count should be age1 ");
        assertEquals(2, topAgeGroups.get(1).getCount(), "Age1 count should be 2");
    }

    protected static <T> List<T> iterableToList(Iterable<T> iterable) {
        List<T> l = new ArrayList<>();
        iterable.forEach(l::add);
        return l;
    }

    protected static Organizer createOrganizer(Long id, String name, int followers){
        Organizer organizer = new Organizer();
        organizer.setId(id);
        organizer.setName(name);
        organizer.setFollowers(followers);
        organizer.setAverageAge(0.0);
        organizer.setMaleFollowers(0);
        organizer.setFemaleFollowers(0);
        organizer.setOtherFollowers(0);
        organizer.setPostedEvents(new HashSet<>());
        organizer.setAgeCounts(new ArrayList<>());
        return organizer;
    }

}