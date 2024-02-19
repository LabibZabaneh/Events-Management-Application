package com.event.management.events.repositories;

import com.event.management.events.domain.Business;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

@Repository
public interface BusinessRepository extends CrudRepository<Business, Long> {
}
