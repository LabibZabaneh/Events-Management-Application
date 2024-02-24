package com.event.management.registrations.repositories;

import com.event.management.registrations.domain.User;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

@Repository
public interface UsersRepository extends CrudRepository<User, Long> {
}
