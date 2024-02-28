package com.event.management.events.repositories;

import com.event.management.events.domain.Organizer;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

@Repository
public interface OrganizersRepository extends CrudRepository<Organizer, Long> {
}
