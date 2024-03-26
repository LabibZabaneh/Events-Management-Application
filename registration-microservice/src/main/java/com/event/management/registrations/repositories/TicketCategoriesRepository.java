package com.event.management.registrations.repositories;

import com.event.management.registrations.domain.TicketCategory;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

@Repository
public interface TicketCategoriesRepository extends CrudRepository<TicketCategory, Long> {

}
