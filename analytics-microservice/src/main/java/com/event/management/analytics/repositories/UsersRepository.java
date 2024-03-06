package com.event.management.analytics.repositories;

import com.event.management.analytics.domain.User;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

@Repository
public interface UsersRepository extends CrudRepository<User, Long> {
}