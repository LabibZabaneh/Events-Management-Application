package com.event.management.registrations.repositories;

import com.event.management.registrations.domain.Event;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

@Repository
public interface EventsRepository extends CrudRepository<Event, Long> {
}
