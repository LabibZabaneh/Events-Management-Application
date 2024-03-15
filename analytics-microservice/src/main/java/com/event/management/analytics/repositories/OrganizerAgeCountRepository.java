package com.event.management.analytics.repositories;

import com.event.management.analytics.domain.OrganizerAgeCount;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

@Repository
public interface OrganizerAgeCountRepository extends CrudRepository<OrganizerAgeCount, Long> {
}
