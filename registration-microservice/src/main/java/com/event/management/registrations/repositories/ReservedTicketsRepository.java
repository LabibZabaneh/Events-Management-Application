package com.event.management.registrations.repositories;

import com.event.management.registrations.domain.Ticket;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

@Repository
public interface ReservedTicketsRepository extends CrudRepository<Ticket, Long> {
}
