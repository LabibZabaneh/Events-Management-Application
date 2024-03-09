package com.event.management.registrations.controllers;

import com.event.management.registrations.domain.Event;
import com.event.management.registrations.domain.User;
import com.event.management.registrations.kafka.producers.RegistrationProducer;
import com.event.management.registrations.repositories.EventsRepository;
import com.event.management.registrations.repositories.UsersRepository;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Put;
import jakarta.inject.Inject;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.Set;

@Controller("/registrations")
public class RegistrationController {

    @Inject
    UsersRepository usersRepo;

    @Inject
    EventsRepository eventsRepo;

    @Inject
    RegistrationProducer producer;

    @Get("/users")
    public Iterable<User> getUsers(){
        return usersRepo.findAll();
    }

    @Get("/events")
    public Iterable<Event> getEvents(){
        return eventsRepo.findAll();
    }

    @Get("/users/{id}")
    public Set<Event> getUserRegistrations(long id){
        Optional<User> oUser = usersRepo.findById(id);
        return oUser.map(User::getRegisteredEvents).orElse(null); // returns the registered events if user is found else returns null
    }

    @Get("/events/{id}")
    public Set<User> getEventRegistrations(long id){
        Optional<Event> oEvent = eventsRepo.findById(id);
        return oEvent.map(Event::getRegisteredUsers).orElse(null);
    }

    @Transactional
    @Put("/users/{eventId}/{userId}")
    public HttpResponse<Void> addRegistration(long eventId, long userId){
        Optional<Event> oEvent = eventsRepo.findById(eventId);
        Optional<User> oUser = usersRepo.findById(userId);
        if (oEvent.isEmpty() || oUser.isEmpty()){
            return HttpResponse.notFound();
        }

        Event e = oEvent.get();
        User u = oUser.get();
        e.getRegisteredUsers().add(u);
        u.getRegisteredEvents().add(e);
        eventsRepo.update(e);
        usersRepo.update(u);

        producer.addedRegistration(userId, eventId);

        return HttpResponse.ok();
    }

    @Transactional
    @Delete("/users/{eventId}/{userId}")
    public HttpResponse<Void> deleteRegistration(long eventId, long userId){
        Optional<Event> oEvent = eventsRepo.findById(eventId);
        Optional<User> oUser = usersRepo.findById(userId);
        if (oEvent.isEmpty() || oUser.isEmpty()){
            return HttpResponse.notFound();
        }

        Event event = oEvent.get();
        User user = oUser.get();

        if (event.getRegisteredUsers().removeIf(u -> userId == u.getId()) && user.getRegisteredEvents().removeIf(e -> eventId == e.getId())){
            producer.addedUnRegistration(userId, eventId);
        }

        eventsRepo.update(event);
        usersRepo.update(user);

        return HttpResponse.ok();
    }
}
