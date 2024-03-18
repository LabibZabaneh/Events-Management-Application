package com.event.management.analytics;

import com.event.management.analytics.domain.Event;
import com.event.management.analytics.domain.Organizer;
import com.event.management.analytics.domain.OrganizerAgeCount;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.annotation.Client;

import java.util.List;
import java.util.Map;

@Client("/analytics")
public interface OrgainzersAnalyticsClient {

    @Get("/organizers")
    Iterable<Organizer> getOrganizers();

    @Get("/organizers/popular")
    List<Organizer> getPopularOrganizers();

    @Get("/organizers/{id}/popular-events")
    List<Event> getOrganizerPopularEvents(long id);

    @Get("/organizers/{id}/registrations-age-distribution")
    Map<Integer, Integer> getOrganizerRegistrationsAgeDistribution(long id);

    @Get("/organizers/{id}/registrations-average-age")
    double getOrganizerRegistrationsAverageAge(long id);

    @Get("/organizers/{id}/followers-age-distribution")
    List<OrganizerAgeCount> getOrganizerFollowersAgeDistribution(long id);

    @Get("/organizers/{id}/followers-average-age")
    double getOrganizerFollowersAverageAge(long id);

    @Get("/organizers/{id}/followers-top-age-groups/{limit}")
    List<OrganizerAgeCount> getOrganizerFollowersTopAgeGroup(long id, int limit);

}