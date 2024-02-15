package com.event.management.events.controllers;

import com.event.management.events.domain.Event;
import com.event.management.events.domain.User;
import com.event.management.events.dto.UserDTO;
import com.event.management.events.repositories.UsersRepository;
import io.micronaut.data.annotation.Repository;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import jakarta.inject.Inject;

import javax.transaction.Transactional;
import java.net.URI;
import java.util.Optional;

@Controller("/users")
public class UsersController {

    @Inject
    UsersRepository repo;

    @Get("/")
    public Iterable<User> list() {
        return repo.findAll();
    }

    @Post("/")
    public HttpResponse<Void> addUser(@Body UserDTO dto){
        User u = new User();
        u.setFirstName(dto.getFirstName());
        u.setLastName(dto.getLastName());
        u.setEmail(dto.getEmail());
        u.setDateOfBirth(dto.getDateOfBirth());
        u.setMobileNumber(dto.getMobileNumber());
        repo.save(u);

        return HttpResponse.created(URI.create("/users/" + u.getId()));
    }

    @Get("/{id}")
    public User getUser(long id) {
        return repo.findById(id).orElse(null);
    }

    @Transactional
    @Put("/{id}")
    public  HttpResponse<Void> updateUser(long id, @Body UserDTO dto){
        Optional<User> oUser = repo.findById(id);
        if (oUser.isEmpty()){
            return HttpResponse.notFound();
        }
        User u = updateUserAttributes(oUser.get(), dto);
        repo.update(u);
        return HttpResponse.ok();
    }

    @Transactional
    @Delete("/{id}")
    public HttpResponse<Void> deleteUser(long id) {
        Optional<User> oUser = repo.findById(id);
        if (oUser.isEmpty()){
            return HttpResponse.notFound();
        }
        repo.delete(oUser.get());
        return HttpResponse.ok();
    }

    private User updateUserAttributes(User u, UserDTO dto) {
        if (dto.getFirstName() != null){
            u.setFirstName(dto.getFirstName());
        }
        if (dto.getLastName() != null){
            u.setLastName(dto.getLastName());
        }
        if (dto.getEmail() != null){
            u.setEmail(dto.getEmail());
        }
        if (dto.getMobileNumber() != null){
            u.setMobileNumber(dto.getMobileNumber());
        }
        if (dto.getDateOfBirth() != null){
            u.setDateOfBirth(dto.getDateOfBirth());
        }
        return u;
    }
}
