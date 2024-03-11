package com.event.management.analytics.repositories;

import com.event.management.analytics.domain.AgeCount;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

@Repository
public interface AgeCountRepository extends CrudRepository<AgeCount, Long> {
}
