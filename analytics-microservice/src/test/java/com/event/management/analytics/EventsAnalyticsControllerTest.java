package com.event.management.analytics;

import com.event.management.analytics.domain.Event;
import com.event.management.analytics.domain.EventAgeCount;
import com.event.management.analytics.domain.Gender;
import com.event.management.analytics.domain.Organizer;
import com.event.management.analytics.repositories.EventsRepository;
import com.event.management.analytics.repositories.OrganizersRepository;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.event.management.analytics.OrganizersAnalyticsControllerTest.createOrganizer;
import static com.event.management.analytics.OrganizersAnalyticsControllerTest.iterableToList;
import static org.junit.jupiter.api.Assertions.*;

@MicronautTest(transactional = false)
public class EventsAnalyticsControllerTest {

    @Inject
    EventsAnalyticsClient client;

    @Inject
    EventsRepository eventsRepo;

    @Inject
    OrganizersRepository organizersRepo;

    @BeforeEach
    public void clean(){
        eventsRepo.deleteAll();
        organizersRepo.deleteAll();
    }

    @Test
    public void noEvents(){
        assertFalse(client.getEvents().iterator().hasNext(), "Service should not list any events");
    }

    @Test
    public void oneEvent(){
        Organizer organizer = createOrganizer(1L, "York Parties", 0);
        organizersRepo.save(organizer);

        Event event = createEvent(1L, "Freshers", organizer);
        eventsRepo.save(event);

        List<Event> events = iterableToList(client.getEvents());
        assertEquals(1, events.size(),"Should have 1 event listed");
        assertEquals(event.getId(), events.get(0).getId(), "events id's should match");
    }

    @Test
    public void invalidEventAgeDistribution(){
        List<EventAgeCount> ageCounts = client.getEventAgeDistribution(0L);
        assertTrue(ageCounts.isEmpty(), "Should return an empty list for an invalid event");
    }

    @Test
    public void noEventAgeDistribution(){
        Organizer organizer = createOrganizer(1L, "York Parties", 0);
        organizersRepo.save(organizer);

        Event event = createEvent(1L, "Freshers", organizer);
        eventsRepo.save(event);

        assertTrue(client.getEventAgeDistribution(event.getId()).isEmpty(), "Should return an empty list");
    }

    @Test
    public void eventAgeDistribution(){
        Organizer organizer = createOrganizer(1L, "York Parties", 0);
        organizersRepo.save(organizer);

        final int age1 = 18;
        final int age2 = 21;

        Event event = createEvent(1L, "Freshers", organizer);
        event.addRegistration(age1, Gender.MALE);
        event.addRegistration(age1, Gender.MALE);
        event.addRegistration(age2, Gender.MALE);
        eventsRepo.save(event);

        List<EventAgeCount> ageCounts = client.getEventAgeDistribution(event.getId());
        assertEquals(2, ageCounts.size(), "Event should have 2 age groups");
        assertEquals(age1, ageCounts.get(0).getAge(), "The first age should be age1");
        assertEquals(2, ageCounts.get(0).getCount(), "Age1 count should be 2");
        assertEquals(age2, ageCounts.get(1).getAge(), "The second age should be age2");
        assertEquals(1, ageCounts.get(1).getCount(), "Age1 count should be 1");
    }

    @Test
    public void invalidEventAverageAge(){
        assertEquals(0.0, client.getEventAverageAge(0L), "Should return 0.0 for an invalid event");
    }

    @Test
    public void noEventAverageAge(){
        Organizer organizer = createOrganizer(1L, "York Parties", 0);
        organizersRepo.save(organizer);

        Event event = createEvent(1L, "Freshers", organizer);
        eventsRepo.save(event);

        assertEquals(0.0, client.getEventAverageAge(event.getId()), "Should return 0.0 for no registrations");
    }

    @Test
    public void eventAverageAge(){
        Organizer organizer = createOrganizer(1L, "York Parties", 0);
        organizersRepo.save(organizer);

        final int age1 = 18;
        final int age2 = 21;
        Event event = createEvent(1L, "Freshers", organizer);
        event.addRegistration(age1, Gender.MALE);
        event.addRegistration(age1, Gender.MALE);
        event.addRegistration(age2, Gender.MALE);
        eventsRepo.save(event);

        final double expectedAverageAge = (age1 * 2 + age2) / 3;
        assertEquals(expectedAverageAge, client.getEventAverageAge(event.getId()), "Average age should be calculated correctly");
    }

    @Test
    public void invalidEventTopAgeGroups(){
        assertTrue(client.getEventTopAgeGroups(0L, 3).isEmpty(), "Should return an empty list on an invalid event");
    }

    @Test
    public void noEventTopAgeGroups(){
        Organizer organizer = createOrganizer(1L, "York Parties", 0);
        organizersRepo.save(organizer);

        Event event = createEvent(1L, "Freshers", organizer);
        eventsRepo.save(event);

        assertTrue(client.getEventTopAgeGroups(event.getId(), 3).isEmpty(), "Should return an empty list");
    }

    @Test
    public void eventTopAgeGroupsNegativeLimit(){
        Organizer organizer = createOrganizer(1L, "York Parties", 0);
        organizersRepo.save(organizer);

        final int age1 = 18;
        final int age2 = 21;
        Event event = createEvent(1L, "Freshers", organizer);
        event.addRegistration(age1, Gender.MALE);
        event.addRegistration(age2, Gender.MALE);
        eventsRepo.save(event);

        assertTrue(client.getEventTopAgeGroups(event.getId(), -1).isEmpty(), "Should return an empty list on a negative limit");
    }

    @Test
    public void eventTopAgeGroupsUnderLimit(){
        Organizer organizer = createOrganizer(1L, "York Parties", 0);
        organizersRepo.save(organizer);

        final int age1 = 18;
        final int age2 = 21;
        final int age3 = 22;
        Event event = createEvent(1L, "Freshers", organizer);
        event.addRegistration(age1, Gender.MALE);
        event.addRegistration(age1, Gender.MALE);
        event.addRegistration(age1, Gender.MALE);
        event.addRegistration(age2, Gender.MALE);
        event.addRegistration(age2, Gender.MALE);
        event.addRegistration(age3, Gender.MALE);
        eventsRepo.save(event);

        List<EventAgeCount> ageCounts = client.getEventTopAgeGroups(event.getId(), 2);
        assertEquals(2, ageCounts.size(), "Age counts should have size 2");
        assertEquals(age1, ageCounts.get(0).getAge(), "The first age count should be age1");
        assertEquals(3, ageCounts.get(0).getCount(), "Age1's counts count should be 3");
        assertEquals(age2, ageCounts.get(1).getAge(), "The second age count should be age2");
        assertEquals(2, ageCounts.get(1).getCount(), "Age2's counts count should be 2");
    }

    @Test
    public void eventTopAgeGroupsOverLimit(){
        Organizer organizer = createOrganizer(1L, "York Parties", 0);
        organizersRepo.save(organizer);

        final int age1 = 18;
        Event event = createEvent(1L, "Freshers", organizer);
        event.addRegistration(age1, Gender.MALE);
        eventsRepo.save(event);

        List<EventAgeCount> ageCounts = client.getEventTopAgeGroups(event.getId(), 2);
        assertEquals(1, ageCounts.size(), "Age counts should have size 1");
        assertEquals(age1, ageCounts.get(0).getAge(), "The first age count should be age1");
        assertEquals(1, ageCounts.get(0).getCount(), "Age1's counts count should be 3");
    }

    @Test
    public void invalidEventGenderDistribution(){
        assertTrue(client.getEventGenderDistribution(0L).isEmpty(), "Should return an empty map on an invalid event");
    }

    @Test
    public void noEventGenderDistribution(){
        Organizer organizer = createOrganizer(1L, "York Parties", 0);
        organizersRepo.save(organizer);

        Event event = createEvent(1L, "Freshers", organizer);
        eventsRepo.save(event);

        Map<Gender, Integer> genderDistribution = client.getEventGenderDistribution(event.getId());
        assertEquals(3, genderDistribution.size(), "Should have 3 genders as keys");
        assertEquals(0, genderDistribution.get(Gender.MALE), "Should have MALE gender count as 0");
        assertEquals(0, genderDistribution.get(Gender.FEMALE), "Should have FEMALE gender count as 0");
        assertEquals(0, genderDistribution.get(Gender.OTHER), "Should have OTHER gender count as 0");
    }

    @Test
    public void eventGenderDistribution(){
        Organizer organizer = createOrganizer(1L, "York Parties", 0);
        organizersRepo.save(organizer);

        Event event = createEvent(1L, "Freshers", organizer);
        event.addRegistration(18, Gender.MALE);
        event.addRegistration(18, Gender.MALE);
        event.addRegistration(18, Gender.FEMALE);
        event.addRegistration(18, Gender.OTHER);
        eventsRepo.save(event);

        Map<Gender, Integer> genderDistribution = client.getEventGenderDistribution(event.getId());
        assertEquals(3, genderDistribution.size(), "Should have 3 genders as keys");
        assertEquals(2, genderDistribution.get(Gender.MALE), "Should have MALE gender count as 2");
        assertEquals(1, genderDistribution.get(Gender.FEMALE), "Should have FEMALE gender count as 1");
        assertEquals(1, genderDistribution.get(Gender.OTHER), "Should have OTHER gender count as 1");
    }

    @Test
    public void invalidEventGenderRatios(){
        assertTrue(client.getEventGenderRatios(0L).isEmpty(), "Should return an empty map on an invalid event");
    }

    @Test
    public void noEventGenderRatios(){
        Organizer organizer = createOrganizer(1L, "York Parties", 0);
        organizersRepo.save(organizer);

        Event event = createEvent(1L, "Freshers", organizer);
        eventsRepo.save(event);

        Map<Gender, Double> genderRatios = client.getEventGenderRatios(event.getId());
        assertEquals(3, genderRatios.size(), "Should have 3 genders as keys");
        assertEquals(0.0, genderRatios.get(Gender.MALE), "Should have MALE gender ratio as 0.0");
        assertEquals(0.0, genderRatios.get(Gender.FEMALE), "Should have FEMALE gender ratio as 0.0");
        assertEquals(0.0, genderRatios.get(Gender.OTHER), "Should have OTHER gender ratio as 0.0");
    }

    @Test
    public void eventGenderRatios(){
        Organizer organizer = createOrganizer(1L, "York Parties", 0);
        organizersRepo.save(organizer);

        Event event = createEvent(1L, "Freshers", organizer);
        event.addRegistration(18, Gender.MALE);
        event.addRegistration(18, Gender.MALE);
        event.addRegistration(18, Gender.MALE);
        event.addRegistration(18, Gender.FEMALE);
        event.addRegistration(18, Gender.OTHER);
        eventsRepo.save(event);

        Map<Gender, Double> genderRatios = client.getEventGenderRatios(event.getId());
        assertEquals(3, genderRatios.size(), "Should have 3 genders as keys");
        assertEquals(60.0, genderRatios.get(Gender.MALE), "Should have MALE gender ratio as 60.0");
        assertEquals(20.0, genderRatios.get(Gender.FEMALE), "Should have FEMALE gender ratio as 20.0");
        assertEquals(20.0, genderRatios.get(Gender.OTHER), "Should have OTHER gender ratio as 20.0");
    }

    protected static Event createEvent(Long id, String name, Organizer organizer){
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
