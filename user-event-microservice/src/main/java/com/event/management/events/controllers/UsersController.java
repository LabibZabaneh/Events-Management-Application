package com.event.management.events.controllers;

import com.event.management.events.domain.Event;
import com.event.management.events.domain.User;
import com.event.management.events.repositories.UsersRepository;
import io.micronaut.data.annotation.Repository;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import jakarta.inject.Inject;

@Repository
public class UsersController {

    @Inject
    UsersRepository repo;

    @Get("/")
    public Iterable<User> list() {
        return repo.findAll();
    }

    @Get("/{id}")
    public User getUser(long id) {
        return repo.findById(id).orElse(null);
    }
}
