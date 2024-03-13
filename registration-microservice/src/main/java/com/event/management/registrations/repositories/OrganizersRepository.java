package com.event.management.registrations.repositories;

import com.event.management.registrations.domain.Organizer;
import com.event.management.registrations.domain.User;
import io.micronaut.data.annotation.Join;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import javax.validation.constraints.NotNull;
import java.util.Optional;

@Repository
public interface OrganizersRepository extends CrudRepository<Organizer, Long> {

    @Join(value = "followers", type = Join.Type.LEFT_FETCH)
    @Override
    Optional<Organizer> findById(@NotNull Long id);

}
