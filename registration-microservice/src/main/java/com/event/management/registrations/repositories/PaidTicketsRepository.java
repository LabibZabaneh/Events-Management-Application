package com.event.management.registrations.repositories;

import com.event.management.registrations.domain.Ticket;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

@Repository
public interface PaidTicketsRepository extends CrudRepository<Ticket, Long> {
}
