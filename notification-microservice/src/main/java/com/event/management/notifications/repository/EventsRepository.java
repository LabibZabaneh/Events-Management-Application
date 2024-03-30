package com.event.management.notifications.repository;

import com.event.management.notifications.domain.Event;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

@Repository
public interface EventsRepository extends CrudRepository<Event, Long> {
}
