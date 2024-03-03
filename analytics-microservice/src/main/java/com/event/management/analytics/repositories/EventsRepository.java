package com.event.management.analytics.repositories;

import com.event.management.analytics.domain.Event;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.util.List;

@Repository
public interface EventsRepository extends CrudRepository<Event, Long> {

    List<Event> findTop10ByOrderByRegistrationsDesc();

}
