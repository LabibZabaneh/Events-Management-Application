package com.event.management.events.repositories;

import com.event.management.events.domain.TicketCategory;
import io.micronaut.data.repository.CrudRepository;

public interface TicketCategoriesRepository extends CrudRepository<TicketCategory, Long> {
}
