package com.event.management.registrations.controllers;

import com.event.management.registrations.domain.Event;
import com.event.management.registrations.domain.Ticket;
import com.event.management.registrations.domain.TicketCategory;
import com.event.management.registrations.domain.User;
import com.event.management.registrations.kafka.producers.RegistrationProducer;
import com.event.management.registrations.repositories.EventsRepository;
import com.event.management.registrations.repositories.TicketCategoriesRepository;
import com.event.management.registrations.repositories.TicketsRepository;
import com.event.management.registrations.repositories.UsersRepository;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Put;
import jakarta.inject.Inject;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Controller("/registrations")
public class RegistrationsController {

    @Inject
    UsersRepository usersRepo;

    @Inject
    EventsRepository eventsRepo;

    @Inject
    TicketsRepository ticketsRepo;

    @Inject
    TicketCategoriesRepository ticketCategoriesRepo;

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

    @Get("/users/{id}/tickets")
    public Set<Ticket> getUserTickets(long id){
        Optional<User> oUser = usersRepo.findById(id);
        return oUser.map(User::getTickets).orElse(null);
    }

    @Get("/events/{id}/ticket-categories")
    public List<TicketCategory> getEventTicketCategories(long id){
        Optional<Event> oEvent = eventsRepo.findById(id);
        return oEvent.map(Event::getTicketCategories).orElse(null);
    }

    @Transactional
    @Put("/users/{userId}/{eventId}/{ticketCategoryId}")
    public Long addRegistration(long userId, long eventId, long ticketCategoryId){
        Optional<User> oUser = usersRepo.findById(userId);
        Optional<Event> oEvent = eventsRepo.findById(eventId);
        Optional<TicketCategory> oTicketCategory = ticketCategoriesRepo.findById(ticketCategoryId);

        if (oUser.isEmpty()){
            return null;
        }
        if (oEvent.isEmpty()){
            return null;
        }
        if (oTicketCategory.isEmpty()){
            return null;
        }

        User user = oUser.get();
        Event event = oEvent.get();
        TicketCategory ticketCategory = oTicketCategory.get();

        if (!ticketCategory.getEvent().equals(event) && event.getTicketCategories().contains(ticketCategory)){
            return null;
        }

        return register(user, ticketCategory);
    }

    @Transactional
    @Delete("/users/{userId}/{eventId}/{ticketCategoryId}")
    public HttpResponse<String> deleteRegistration(long eventId, long userId, long ticketCategoryId){
        Optional<User> oUser = usersRepo.findById(userId);
        Optional<Event> oEvent = eventsRepo.findById(eventId);
        Optional<TicketCategory> oTicketCategory = ticketCategoriesRepo.findById(ticketCategoryId);

        if (oUser.isEmpty()){
            return HttpResponse.notFound("User not Found");
        }
        if (oEvent.isEmpty()){
            return HttpResponse.notFound("Event not Found");
        }
        if (oTicketCategory.isEmpty()){
            return HttpResponse.notFound("Ticket category not Found");
        }

        User user = oUser.get();
        Event event = oEvent.get();
        TicketCategory ticketCategory = oTicketCategory.get();

        if (!ticketCategory.getEvent().equals(event) && event.getTicketCategories().contains(ticketCategory)){
            return HttpResponse.badRequest("Ticket category does not belong to provided event");
        }

        Optional<Ticket> oTicketToRemove = user.getTickets().stream().filter(ticket -> ticket.getTicketCategory().equals(ticketCategory)).findFirst();

        if (oTicketToRemove.isEmpty()) {
            return HttpResponse.notFound("Ticket not found");
        }

        Ticket ticketToRemove = oTicketToRemove.get();
        ticketsRepo.delete(ticketToRemove);

        user.getTickets().remove(ticketToRemove);
        usersRepo.update(user);

        ticketCategory.getSoldTickets().remove(ticketToRemove);
        ticketCategoriesRepo.update(ticketCategory);

        return HttpResponse.ok();
    }

    private Long register(User user, TicketCategory ticketCategory){
        Ticket reservedTicket = new Ticket();
        reservedTicket.setUser(user);
        reservedTicket.setTicketCategory(ticketCategory);
        ticketsRepo.save(reservedTicket);

        ticketCategory.getReservedTickets().add(reservedTicket);
        ticketCategoriesRepo.update(ticketCategory);

        System.out.println("Ticket " + reservedTicket.getId() + " has been reserved for User " + user.getId() + " to Event " + ticketCategory.getEvent().getId());

        return reservedTicket.getId();
    }
}