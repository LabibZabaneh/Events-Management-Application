package com.event.management.registrations.repositories;

import com.event.management.registrations.domain.Business;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

@Repository
public interface BusinessRepository extends CrudRepository<Business, Long> {
}
