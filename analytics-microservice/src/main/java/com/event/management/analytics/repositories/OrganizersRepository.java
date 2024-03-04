package com.event.management.analytics.repositories;

import com.event.management.analytics.domain.Organizer;
import com.event.management.analytics.dto.OrganizerDTO;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

@Repository
public interface OrganizersRepository extends CrudRepository<Organizer, Long> {
}
