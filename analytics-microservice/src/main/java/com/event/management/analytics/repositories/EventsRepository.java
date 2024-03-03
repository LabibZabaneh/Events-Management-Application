package com.event.management.analytics.repositories;

import com.event.management.analytics.domain.Event;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

@Repository
public interface EventsRepository extends CrudRepository<Event, Long> {
}
