package com.event.management.registrations;

import com.event.management.registrations.clients.RegistrationsClient;
import com.event.management.registrations.domain.Event;
import com.event.management.registrations.domain.Ticket;
import com.event.management.registrations.domain.TicketCategory;
import com.event.management.registrations.domain.User;
import com.event.management.registrations.repositories.EventsRepository;
import com.event.management.registrations.repositories.TicketCategoriesRepository;
import com.event.management.registrations.repositories.TicketsRepository;
import com.event.management.registrations.repositories.UsersRepository;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest(transactional = false)
public class RegistrationControllerTest {

    @Inject
    RegistrationsClient client;

    @Inject
    EventsRepository eventsRepo;

    @Inject
    UsersRepository usersRepo;

    @Inject
    TicketsRepository ticketsRepo;

    @Inject
    TicketCategoriesRepository ticketCategoriesRepo;

    @BeforeEach
    public void clean(){
        ticketsRepo.deleteAll();
        ticketCategoriesRepo.deleteAll();
        eventsRepo.deleteAll();
        usersRepo.deleteAll();

    }

    @Test
    public void invalidIdUserTickets(){
        assertNull(client.getUserTickets(0L), "Should return null on an invalid user");
    }

    @Test
    public void noUserTickets(){
        User user = createUser();
        usersRepo.save(user);

        assertTrue(client.getUserTickets(user.getId()).isEmpty(), "User should not have any tickets");
    }

    @Test
    public void userTickets(){
        User user = createUser();
        usersRepo.save(user);

        Event event = createEvent();
        eventsRepo.save(event);

        TicketCategory ticketCategory = createTicketCategory(event);
        ticketCategoriesRepo.save(ticketCategory);

        event.getTicketCategories().add(ticketCategory);
        eventsRepo.update(event);

        Ticket ticket = new Ticket();
        ticket.setUser(user);
        ticket.setTicketCategory(ticketCategory);
        ticketsRepo.save(ticket);

        ticketCategory.getSoldTickets().add(ticket);
        ticketCategoriesRepo.update(ticketCategory);

        user.getTickets().add(ticket);
        usersRepo.update(user);

        Set<Ticket> tickets = client.getUserTickets(user.getId());
        assertEquals(1, tickets.size(), "User should only have one ticket");
        assertEquals(ticket.getId(), tickets.iterator().next().getId(), "Tickets should match ids");
    }

    @Test
    public void invalidEventTicketCategories(){
        assertNull(client.getEventTicketCategories(0L), "Should return null on an invalid event");
    }

    @Test
    public void noEventTicketCategories(){
        Event event = createEvent();
        eventsRepo.save(event);

        assertTrue(client.getEventTicketCategories(event.getId()).isEmpty(), "Event should have no ticket Categories");
    }

    @Test
    public void eventTicketCategories(){
        Event event = createEvent();
        eventsRepo.save(event);

        TicketCategory ticketCategory = createTicketCategory(event);
        ticketCategoriesRepo.save(ticketCategory);

        event.getTicketCategories().add(ticketCategory);
        eventsRepo.update(event);

        List<TicketCategory> ticketCategories = client.getEventTicketCategories(event.getId());
        assertEquals(1, ticketCategories.size(), "Event should have only one ticket category");
        assertEquals(ticketCategory.getId(), ticketCategories.get(0).getId(), "Ticket Category ids should be equal");
    }

    @Test
    public void addRegistrationWithInvalidUser(){
        Event event = createEvent();
        eventsRepo.save(event);

        TicketCategory ticketCategory = createTicketCategory(event);
        ticketCategoriesRepo.save(ticketCategory);

        event.getTicketCategories().add(ticketCategory);
        eventsRepo.update(event);

        assertNull(client.addRegistration(0L, event.getId(), ticketCategory.getId()), "Should return null on an invalid user");
    }

    @Test
    public void addRegistrationWithInvalidEvent(){
        User user = createUser();
        usersRepo.save(user);

        Event event = createEvent();
        eventsRepo.save(event);

        TicketCategory ticketCategory = createTicketCategory(event);
        ticketCategoriesRepo.save(ticketCategory);

        event.getTicketCategories().add(ticketCategory);
        eventsRepo.update(event);

        // Used event id 0 (invalid)
        assertNull(client.addRegistration(user.getId(), 0L, ticketCategory.getId()), "Should return null on an invalid event");
    }

    @Test
    public void addRegistrationWithInvalidTicketCategory(){
        User user = createUser();
        usersRepo.save(user);

        Event event = createEvent();
        eventsRepo.save(event);

        assertNull(client.addRegistration(user.getId(), event.getId(), 0), "Should return null on an invalid ticket category");
    }

    @Test
    public void addRegistrationWithTicketCategoryNotForEvent(){
        User user = createUser();
        usersRepo.save(user);

        Event event1 = createEvent();
        eventsRepo.save(event1);

        Event event2 = createEvent();
        eventsRepo.save(event2);

        TicketCategory ticketCategory = createTicketCategory(event1);
        ticketCategoriesRepo.save(ticketCategory);

        event1.getTicketCategories().add(ticketCategory);
        eventsRepo.update(event1);

        assertNull(client.addRegistration(user.getId(), event2.getId(), ticketCategory.getId()), "Should return null on ticket category not for event");
    }

    @Test
    public void addRegistrationWithNoAvailableTickets(){
        User user = createUser();
        usersRepo.save(user);

        Event event = createEvent();
        eventsRepo.save(event);

        TicketCategory ticketCategory = createTicketCategory(event);
        ticketCategoriesRepo.save(ticketCategory);

        event.getTicketCategories().add(ticketCategory);
        eventsRepo.update(event);

        Ticket ticket = new Ticket();
        ticket.setUser(user);
        ticket.setTicketCategory(ticketCategory);
        ticketsRepo.save(ticket);

        ticketCategory.getSoldTickets().add(ticket);
        ticketCategoriesRepo.update(ticketCategory);

        user.getTickets().add(ticket);
        usersRepo.update(user);

        assertNull(client.addRegistration(user.getId(), event.getId(), ticketCategory.getId()), "Should return null if no tickets are available");
    }

    @Test
    public void addRegistration(){
        User user = createUser();
        usersRepo.save(user);

        Event event = createEvent();
        eventsRepo.save(event);

        TicketCategory ticketCategory = createTicketCategory(event);
        ticketCategoriesRepo.save(ticketCategory);

        event.getTicketCategories().add(ticketCategory);
        eventsRepo.update(event);

        Long reservedTicketId = client.addRegistration(user.getId(), event.getId(), ticketCategory.getId());

        Optional<Ticket> oTicket = ticketsRepo.findById(reservedTicketId);
        assertTrue(oTicket.isPresent(), "Ticket should be generated");;

        List<Ticket> reservedTickets = ticketCategoriesRepo.findById(ticketCategory.getId()).get().getReservedTickets();
        assertEquals(1, reservedTickets.size(), "Ticket category should have one reserved ticket");
        assertEquals(reservedTicketId, reservedTickets.get(0).getId(), "Ticket should have the correct ticket id");

    }

    @Test
    public void deleteRegistrationWithInvalidUser(){
        Event event = createEvent();
        eventsRepo.save(event);

        TicketCategory ticketCategory = createTicketCategory(event);
        ticketCategoriesRepo.save(ticketCategory);

        event.getTicketCategories().add(ticketCategory);
        eventsRepo.update(event);

        assertEquals(HttpStatus.NOT_FOUND, client.deleteRegistration(event.getId(), 0L, ticketCategory.getId()).getStatus(), "Should return http status not found on an invalid user");
    }

    @Test
    public void deleteRegistrationWithInvalidEvent(){
        User user = createUser();
        usersRepo.save(user);

        Event event = createEvent();
        eventsRepo.save(event);

        TicketCategory ticketCategory = createTicketCategory(event);
        ticketCategoriesRepo.save(ticketCategory);

        event.getTicketCategories().add(ticketCategory);
        eventsRepo.update(event);

        // Used event id 0 (invalid)
        assertEquals(HttpStatus.NOT_FOUND, client.deleteRegistration(0L, user.getId(), ticketCategory.getId()).getStatus(), "Should return http status not found on an invalid event");
    }

    @Test
    public void deleteRegistrationWithInvalidTicketCategory(){
        User user = createUser();
        usersRepo.save(user);

        Event event = createEvent();
        eventsRepo.save(event);

        assertEquals(HttpStatus.NOT_FOUND, client.deleteRegistration(event.getId(), user.getId(), 0L).getStatus(), "Should return http status not found on an invalid ticket category");
    }

    @Test
    public void deleteRegistrationWithTicketNotForEvent(){
        User user = createUser();
        usersRepo.save(user);

        Event event1 = createEvent();
        eventsRepo.save(event1);

        Event event2 = createEvent();
        eventsRepo.save(event2);

        TicketCategory ticketCategory = createTicketCategory(event1);
        ticketCategoriesRepo.save(ticketCategory);

        event1.getTicketCategories().add(ticketCategory);
        eventsRepo.update(event1);

        // Used event2
        assertEquals(HttpStatus.BAD_REQUEST, client.deleteRegistration(event2.getId(), user.getId(), ticketCategory.getId()).getStatus(), "Should return http status bad request for a ticket category not for event");
    }

    @Test
    public void deleteRegistrationWithInvalidTicket(){
        User user = createUser();
        usersRepo.save(user);

        Event event = createEvent();
        eventsRepo.save(event);

        TicketCategory ticketCategory = createTicketCategory(event);
        ticketCategoriesRepo.save(ticketCategory);

        event.getTicketCategories().add(ticketCategory);
        eventsRepo.update(event);

        assertEquals(HttpStatus.NOT_FOUND, client.deleteRegistration(event.getId(), user.getId(), ticketCategory.getId()).getStatus(), "Should return http response not found for an invalid ticket");
    }

    @Test
    public void deleteRegistration(){
        User user = createUser();
        usersRepo.save(user);

        Event event = createEvent();
        eventsRepo.save(event);

        TicketCategory ticketCategory = createTicketCategory(event);
        ticketCategoriesRepo.save(ticketCategory);

        event.getTicketCategories().add(ticketCategory);
        eventsRepo.update(event);

        Ticket ticket = new Ticket();
        ticket.setUser(user);
        ticket.setTicketCategory(ticketCategory);
        ticketsRepo.save(ticket);

        ticketCategory.getSoldTickets().add(ticket);
        ticketCategoriesRepo.update(ticketCategory);

        user.getTickets().add(ticket);
        usersRepo.update(user);

        HttpResponse<String> response = client.deleteRegistration(event.getId(), user.getId(), ticketCategory.getId());
        assertEquals(HttpStatus.OK, response.getStatus(), "Should return http status ok");

        Set<Ticket> repoUserTickets = usersRepo.findById(user.getId()).get().getTickets();
        assertTrue(repoUserTickets.isEmpty(), "User should not have any tickets");

        List<Ticket> repoTicketCategoryTickets = ticketCategoriesRepo.findById(ticketCategory.getId()).get().getSoldTickets();
        assertTrue(repoTicketCategoryTickets.isEmpty(), "Ticket category should not have any sold tickets");
    }

    protected static User createUser(){
        User user = new User();
        user.setId(1L);
        user.setFirstName("Doe");
        user.setEmail("test@test.com");
        user.setFollowedOrganizers(new HashSet<>());
        user.setTickets(new HashSet<>());
        return user;
    }

    private Event createEvent() {
        Event event = new Event();
        event.setId(1L);
        event.setEventName("York Parties");
        event.setTicketCategories(new ArrayList<>());
        return event;
    }

    private TicketCategory createTicketCategory(Event event){
        TicketCategory ticketCategory = new TicketCategory();
        ticketCategory.setName("Standard");
        ticketCategory.setPrice(2.0);
        ticketCategory.setQuantity(1);
        ticketCategory.setEvent(event);
        ticketCategory.setReservedTickets(new ArrayList<>());
        ticketCategory.setSoldTickets(new ArrayList<>());
        return ticketCategory;
    }

    protected static <T> List<T> iterableToList(Iterable<T> iterable) {
        List<T> l = new ArrayList<>();
        iterable.forEach(l::add);
        return l;
    }
}
