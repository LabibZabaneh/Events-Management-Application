package com.event.management.registrations;

import com.event.management.registrations.clients.RegistrationsClient;
import com.event.management.registrations.domain.Event;
import com.event.management.registrations.domain.User;
import com.event.management.registrations.repositories.EventsRepository;
import com.event.management.registrations.repositories.UsersRepository;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest(transactional = false)
public class RegistrationControllerTest {

    @Inject
    RegistrationsClient client;

    @Inject
    EventsRepository eventsRepo;

    @Inject
    UsersRepository usersRepo;

    @BeforeEach
    public void clean(){
        eventsRepo.deleteAll();
        usersRepo.deleteAll();
    }

    @Test
    public void noRegistrations(){
        Event event = createEvent();
        eventsRepo.save(event);

        User user = createUser();
        usersRepo.save(user);

        assertFalse(client.getUserRegistrations(user.getId()).iterator().hasNext(), "There shouldn't be any user registered events");
        assertFalse(client.getEventRegistrations(event.getId()).iterator().hasNext(), "There shouldn't be any event registrations");
    }

    @Test
    public void oneRegistration(){
        Event event = createEvent();
        eventsRepo.save(event);

        User user = createUser();
        usersRepo.save(user);

        event.getRegisteredUsers().add(user);
        user.getRegisteredEvents().add(event);

        eventsRepo.update(event);
        usersRepo.update(user);

        List<User> eventUsers = iterableToList(client.getEventRegistrations(event.getId()));
        assertEquals(eventUsers.size(), 1, "There should only be one user");
        assertEquals(eventUsers.get(0).getId(), user.getId(), "The user id should be the same");

        List<Event> userEvents = iterableToList(client.getUserRegistrations(user.getId()));
        assertEquals(userEvents.size(), 1, "There should only be one event");
        assertEquals(userEvents.get(0).getId(), event.getId(), "The event id should be the same");
    }

    @Test
    public void getRegistrationsWithInvalidIds(){
        assertNull(client.getEventRegistrations(0), "Should return null on an invalid event id");
        assertNull(client.getUserRegistrations(0), "Should return null on an invalid user id");
    }

    @Test
    public void addRegistration(){
        Event event = createEvent();
        eventsRepo.save(event);

        User user = createUser();
        usersRepo.save(user);

        HttpResponse<Void> resp = client.addRegistration(event.getId(), user.getId());
        assertEquals(HttpStatus.OK, resp.getStatus(), "Registering to an event should be successful");

        Event repoEvent = eventsRepo.findById(event.getId()).get();
        assertEquals(1, repoEvent.getRegisteredUsers().size(), "There should be one registered user");
        assertEquals(user.getId(), repoEvent.getRegisteredUsers().iterator().next().getId(), "The users ids should be the same");

        User repoUser = usersRepo.findById(user.getId()).get();
        assertEquals(1, repoUser.getRegisteredEvents().size(), "There should be one registered event");
        assertEquals(event.getId(), repoUser.getRegisteredEvents().iterator().next().getId(), "The events ids should be the same");
    }

    @Test
    public void addRegistrationWithInvalidUser(){
        Event event = createEvent();
        eventsRepo.save(event);

        HttpResponse<Void> resp = client.addRegistration(event.getId(), 0);
        assertEquals(HttpStatus.NOT_FOUND, resp.getStatus(), "Adding an registration with an invalid user should not be successful");

        Event repoEvent = eventsRepo.findById(event.getId()).get();
        assertFalse(repoEvent.getRegisteredUsers().iterator().hasNext(), "Event should have no registrations");
    }

    @Test
    public void addRegistrationWithInvalidEvent(){
        User user = createUser();
        usersRepo.save(user);

        HttpResponse<Void> resp = client.addRegistration(0, user.getId());
        assertEquals(HttpStatus.NOT_FOUND, resp.getStatus(), "Adding an registration with an invalid event should not be successful");

        User repoUser = usersRepo.findById(user.getId()).get();
        assertFalse(repoUser.getRegisteredEvents().iterator().hasNext(), "User should not have any registered events");
    }

    @Test
    public void deleteEventRegistration(){
        Event event = createEvent();
        eventsRepo.save(event);

        User user = createUser();
        usersRepo.save(user);

        event.getRegisteredUsers().add(user);
        user.getRegisteredEvents().add(event);

        eventsRepo.update(event);
        usersRepo.update(user);

        HttpResponse<Void> resp = client.deleteRegistration(event.getId(), user.getId());
        assertEquals(HttpStatus.OK, resp.getStatus(), "Deleting a event registration should be successful");

        Event repoEvent = eventsRepo.findById(event.getId()).get();
        assertFalse(repoEvent.getRegisteredUsers().iterator().hasNext(), "Event should have no registrations");

        User repoUser = usersRepo.findById(user.getId()).get();
        assertFalse(repoUser.getRegisteredEvents().iterator().hasNext(), "User should not have any registered events");
    }

    protected static User createUser(){
        User user = new User();
        user.setId(1L);
        user.setFirstName("Doe");
        user.setEmail("test@test.com");
        user.setFollowedOrganizers(new HashSet<>());
        user.setRegisteredEvents(new HashSet<>());
        return user;
    }

    private Event createEvent() {
        Event event = new Event();
        event.setId(1L);
        event.setEventName("York Parties");
        event.setVenue("Salvation");
        event.setDate("27/10/2023");
        event.setTime("22:00");
        event.setRegisteredUsers(new HashSet<>());
        return event;
    }

    protected static <T> List<T> iterableToList(Iterable<T> iterable) {
        List<T> l = new ArrayList<>();
        iterable.forEach(l::add);
        return l;
    }
}
