package com.event.management.analytics;

import com.event.management.analytics.domain.Organizer;
import com.event.management.analytics.repositories.OrganizersRepository;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        assertFalse(organizersRepo.findAll().iterator().hasNext(), "Service should not have any organizers listed");
    }

    @Test
    public void oneOrganizer(){

    }

    @Test
    public void noPopularOrganizers(){

    }

    @Test
    public void onePopularOrganizer(){

    }

    @Test
    public void tenPopularOrganizers(){

    }

    @Test
    public void elevenPopularOrganizers(){

    }

    @Test
    public void noOrganizerPopularEvents(){

    }

    @Test
    public void oneOrganizerPopularEvent(){

    }
}
