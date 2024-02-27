package com.event.management.registrations.repositories;

import com.event.management.registrations.domain.Organizer;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

@Repository
public interface OrganizersRepository extends CrudRepository<Organizer, Long> {
}
