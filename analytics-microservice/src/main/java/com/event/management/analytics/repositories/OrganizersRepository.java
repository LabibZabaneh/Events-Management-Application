package com.event.management.analytics.repositories;

import com.event.management.analytics.domain.Organizer;
import com.event.management.analytics.dto.OrganizerDTO;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.util.List;

@Repository
public interface OrganizersRepository extends CrudRepository<Organizer, Long> {

    List<Organizer> findTop10ByOrderByFollowersDesc();

}
