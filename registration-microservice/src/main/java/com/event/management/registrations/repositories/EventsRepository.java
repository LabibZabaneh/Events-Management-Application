package com.event.management.registrations.repositories;

import com.event.management.registrations.domain.Event;
import io.micronaut.data.annotation.Join;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import javax.validation.constraints.NotNull;
import java.util.Optional;

@Repository
public interface EventsRepository extends CrudRepository<Event, Long> {

    @Join(value = "registeredUsers",type = Join.Type.LEFT_FETCH)
    @Override
    Optional<Event> findById(@NotNull Long id);

}
