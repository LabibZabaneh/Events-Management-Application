package com.event.management.analytics;

import com.event.management.analytics.domain.Event;
import com.event.management.analytics.domain.EventAgeCount;
import com.event.management.analytics.domain.Gender;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.annotation.Client;

import java.util.List;
import java.util.Map;

@Client("/analytics")
public interface EventsAnalyticsClient {

    @Get("/events")
    Iterable<Event> getEvents();

    @Get("/events/{id}/age-distribution")
    List<EventAgeCount> getEventAgeDistribution(long id);

    @Get("/events/{id}/average-age")
    double getEventAverageAge(long id);

    @Get("/events/{id}/top-age-groups/{limit}")
    List<EventAgeCount> getEventTopAgeGroups(long id, int limit);

    @Get("/popular/events")
    List<Event> getPopularEvents();

    @Get("/events/{id}/gender-distribution")
    Map<Gender, Integer> getEventGenderDistribution(long id);

    @Get("/events/{id}/gender-ratio")
    Map<Gender, Double> getEventGenderRatios(long id);
}
