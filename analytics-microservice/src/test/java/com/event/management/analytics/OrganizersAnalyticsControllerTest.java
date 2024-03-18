package com.event.management.analytics;

import com.event.management.analytics.domain.Organizer;
import com.event.management.analytics.repositories.OrganizersRepository;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@MicronautTest(transactional = false)
public class OrganizersAnalyticsControllerTest {

    @Inject
    OrgainzersAnalyticsClient client;

    @Inject
    OrganizersRepository organizersRepo;

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
    }

    @Test
    public void tenPopularOrganizers(){

    }

    @Test
    public void elevenPopularOrganizers(){

    }

    @Test
    public void invalidOrganizerPopularEvents(){

    }

    @Test
    public void noOrganizerPopularEvents(){

    }

    @Test
    public void organizerSortedPopularEvents(){

    }

    @Test
    public void invalidOrganizerRegistrationsAgeDistribution(){

    }

    @Test
    public void noOrganizerRegistrationsAgeDistribution(){

    }

    @Test
    public void organizerRegistrationsAgeDistribution(){

    }

    @Test
    public void invalidOrganizerRegistrationsAverageAge(){

    }

    @Test
    public void noOrganizerRegistrationsAverageAge(){

    }

    @Test
    public void organizerRegistrationsAverageAge(){

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
        return organizer;
    }

}
