package com.event.management.notifications.repositories;

import com.event.management.notifications.domain.User;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

@Repository
public interface UsersRepository extends CrudRepository<User, Long> {
}
