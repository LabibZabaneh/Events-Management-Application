package com.event.management.analytics.repositories;

import com.event.management.analytics.domain.Event;
import com.event.management.analytics.domain.Organizer;
import com.event.management.analytics.dto.OrganizerDTO;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.util.List;

@Repository
public interface OrganizersRepository extends CrudRepository<Organizer, Long> {

    List<Organizer> findTop10ByOrderByFollowersDesc();

    @Query("SELECT e FROM Event e WHERE e.organizer.id = :organizerId ORDER BY e.registrations DESC")
    List<Event> findTopRegisteredEventsByOrganizerId(Long organizerId, int limit);

}
