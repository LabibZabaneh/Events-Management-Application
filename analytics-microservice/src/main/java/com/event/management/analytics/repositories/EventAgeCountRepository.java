package com.event.management.analytics.repositories;

import com.event.management.analytics.domain.EventAgeCount;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

@Repository
public interface EventAgeCountRepository extends CrudRepository<EventAgeCount, Long> {
}
