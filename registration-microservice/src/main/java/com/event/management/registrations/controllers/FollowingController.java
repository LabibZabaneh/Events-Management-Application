package com.event.management.registrations.controllers;

import com.event.management.registrations.domain.Organizer;
import com.event.management.registrations.domain.User;
import com.event.management.registrations.repositories.OrganizersRepository;
import com.event.management.registrations.repositories.UsersRepository;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Put;
import jakarta.inject.Inject;

import javax.transaction.Transactional;
import java.util.Optional;

@Controller("/following")
public class FollowingController {

    @Inject
    UsersRepository usersRepo;

    @Inject
    OrganizersRepository organizersRepo;

    @Get("/organizers")
    public Iterable<Organizer> getOrganizers(){
        return organizersRepo.findAll();
    }

    @Get("/users")
    public Iterable<User> getUsers(){
        return usersRepo.findAll();
    }

    @Get("/organizers/{id}")
    public Iterable<User> getOrganizerFollowers(long id){
        Optional<Organizer> oOrganizer = organizersRepo.findById(id);
        return oOrganizer.map(Organizer::getFollowers).orElse(null);
    }

    @Get("/users/{id}")
    public Iterable<Organizer> getUserFollowing(long id){
        Optional<User> oUser = usersRepo.findById(id);
        return oUser.map(User::getFollowedOrganizers).orElse(null);
    }

    @Transactional
    @Put("/users/{organizerId}/{userId}")
    public HttpResponse<Void> addFollower(long organizerId, long userId){
        Optional<Organizer> oOrganizer = organizersRepo.findById(organizerId);
        Optional<User> oUser = usersRepo.findById(userId);
        if (oOrganizer.isEmpty() || oUser.isEmpty()){
            return HttpResponse.notFound();
        }
        Organizer organizer = oOrganizer.get();
        User user = oUser.get();
        user.getFollowedOrganizers().add(organizer);
        organizer.getFollowers().add(user);
        usersRepo.update(user);
        organizersRepo.update(organizer);

        return HttpResponse.ok();
    }

    @Transactional
    @Delete("/users/{organizerId}/{userId}")
    public HttpResponse<Void> deleteFollower(long organizerId, long userId){
        Optional<Organizer> oOrganizer = organizersRepo.findById(organizerId);
        Optional<User> oUser = usersRepo.findById(userId);
        if (oOrganizer.isEmpty() || oUser.isEmpty()){
            return HttpResponse.notFound();
        }
        Organizer organizer = oOrganizer.get();
        User user = oUser.get();
        organizer.getFollowers().removeIf(u -> userId == u.getId());
        user.getFollowedOrganizers().removeIf(o -> organizerId == o.getId());

        usersRepo.update(user);
        organizersRepo.update(organizer);

        return HttpResponse.ok();
    }
}
