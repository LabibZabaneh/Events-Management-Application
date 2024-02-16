package com.event.management.events;

import com.event.management.events.domain.User;
import com.event.management.events.dto.UserDTO;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.http.client.annotation.Client;

@Client("${users.url:`http://localhost:8080/users`}")
public interface UsersClient {

    @Get("/")
    Iterable<User> list();

    @Post("/")
    HttpResponse<Void> addUser(@Body UserDTO dto);

    @Get("/{id}")
    User getUser(long id);

    @Put("/{id}")
    HttpResponse<Void> updateUser(long id, @Body UserDTO dto);

    @Delete("/{id}")
    HttpResponse<Void> deleteUser(long id);
}
