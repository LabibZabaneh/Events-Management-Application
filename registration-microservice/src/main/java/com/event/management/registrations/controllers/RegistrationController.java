package com.event.management.registrations.controllers;

import com.event.management.registrations.domain.Event;
import com.event.management.registrations.domain.Ticket;
import com.event.management.registrations.domain.TicketCategory;
import com.event.management.registrations.domain.User;
import com.event.management.registrations.kafka.producers.RegistrationProducer;
import com.event.management.registrations.repositories.EventsRepository;
import com.event.management.registrations.repositories.ReservedTicketsRepository;
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
    ReservedTicketsRepository reservedTicketsRepo;

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

        User user = usersRepo.findById(userId).get();
        Event event = eventsRepo.findById(eventId).get();

        return register(user, event, ticketCategory);
    }

    // TODO Potentially remove the deletion of registrations
    @Transactional
    @Delete("/users/{userId}/{eventId}/{ticketCategory}")
    public HttpResponse<String> deleteRegistration(long eventId, long userId, String ticketCategory){
        HttpResponse<String> validation = validateInputs(userId, eventId, ticketCategory);
        if (validation != null){
            return validation;
        }
        User user = usersRepo.findById(userId).get();
        Event event = eventsRepo.findById(eventId).get();
        HttpResponse<String> validateTicketCategory = validateTicketCategory(event, ticketCategory);
        if (validateTicketCategory != null){
            return validateTicketCategory;
        }
        Ticket ticketToRemove = findTicketToRemove(user, event, ticketCategory);
        if (ticketToRemove == null) {
            return HttpResponse.notFound("Ticket not found for user, event, and ticket category");
        }
        if (event.getSoldTickets().contains(ticketToRemove)){
            return HttpResponse.notFound("Event does not have the ticket");
        }
        updateUnregistrationsEntities(user, event, ticketToRemove); // remove tickets from user, event and delete ticket
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
                    reservedTicketsRepo.save(ticket);
                    updateRegistrationEntities(user, event, ticket);
                    category.incrementReservedTicketCount();
                    return HttpResponse.ok();
                }
            }
        }
        return HttpResponse.notFound("Ticket category not found");
    }

    private void updateRegistrationEntities(User user, Event event, Ticket ticket){
        event.getReservedTickets().add(ticket);
        user.getReservedTickets().add(ticket);
        event.getRegisteredUsers().add(user);
        user.getRegisteredEvents().add(event);
        eventsRepo.update(event);
        usersRepo.update(user);
    }

    private Ticket findTicketToRemove(User user, Event event, String ticketCategory){
        for (Ticket ticket: user.getTickets()){
            if (ticket.getEvent().getId().equals(event.getId()) && ticket.getTicketCategory().getName().equals(ticketCategory)){
                return ticket;
            }
        }
        return null;
    }

    private HttpResponse<String> validateTicketCategory(Event event, String ticketCategory){
        for (TicketCategory category: event.getTicketCategories()){
            if (category.getName().equals(ticketCategory)){
                if (category.getSoldTicketsCount() <= 0){
                    return HttpResponse.notFound("No tickets were sold");
                }
            }
        }
        return null;
    }

    private void updateUnregistrationsEntities(User user, Event event, Ticket ticketToRemove){
        event.getSoldTickets().remove(ticketToRemove);
        user.getTickets().remove(ticketToRemove);
        eventsRepo.update(event);
        usersRepo.update(user);
        reservedTicketsRepo.delete(ticketToRemove);
        producer.addedUnRegistration(user.getId(), event.getId());
    }
}