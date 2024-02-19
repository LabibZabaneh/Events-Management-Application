package com.event.management.events.repositories;

import com.event.management.events.domain.Event;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

@Repository
public interface EventsRepository extends CrudRepository<Event, Long> {
}
