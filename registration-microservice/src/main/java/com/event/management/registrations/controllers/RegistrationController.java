package com.event.management.registrations.controllers;

import com.event.management.registrations.domain.Event;
import com.event.management.registrations.domain.Ticket;
import com.event.management.registrations.domain.TicketCategory;
import com.event.management.registrations.domain.User;
import com.event.management.registrations.kafka.producers.RegistrationProducer;
import com.event.management.registrations.repositories.EventsRepository;
import com.event.management.registrations.repositories.TicketsRepository;
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
    TicketsRepository ticketsRepo;

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
    @Put("/users/{userId}/{eventId}/{ticketCategory}")
    public HttpResponse<String> addRegistration(long userId, long eventId, String ticketCategory){
        HttpResponse<String> validation = validateInputs(userId, eventId, ticketCategory);
        if (validation != null){
            return validation;
        }

        User u = usersRepo.findById(userId).get();
        Event e = eventsRepo.findById(eventId).get();

        return register(u, e, ticketCategory);
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

    private HttpResponse<String> validateInputs(Long userId, Long eventId, String ticketCategory){
        if (!usersRepo.existsById(userId)){
            return HttpResponse.notFound("User not Found");
        }
        if (!eventsRepo.existsById(eventId)){
            return HttpResponse.notFound("Event not Found");
        }
        if (ticketCategory == null){
            return HttpResponse.badRequest("Ticket category is null");
        }
        return null;
    }

    private HttpResponse<String> register(User user, Event event, String ticketCategory){
        for (TicketCategory category : event.getTicketCategories()){
            if (category.getName().equals(ticketCategory)){
                if (!category.areTicketsAvailable()){
                    return HttpResponse.notFound("No tickets available");
                } else {
                    Ticket ticket = new Ticket(event, user, category);
                    ticketsRepo.save(ticket);
                    updateRegistrationEntities(user, event, ticket);
                    category.incrementSoldTicketCount();
                    producer.addedRegistration(user.getId(), event.getId());
                    return HttpResponse.ok();
                }
            }
        }
        return HttpResponse.notFound("Ticket category not found");
    }

    private void updateRegistrationEntities(User user, Event event, Ticket ticket){
        event.getSoldTickets().add(ticket);
        user.getTickets().add(ticket);
        event.getRegisteredUsers().add(user);
        user.getRegisteredEvents().add(event);
        eventsRepo.update(event);
        usersRepo.update(user);
    }
}
