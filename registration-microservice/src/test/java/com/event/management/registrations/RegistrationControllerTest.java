package com.event.management.registrations;

import com.event.management.registrations.clients.RegistrationsClient;
import com.event.management.registrations.domain.Event;
import com.event.management.registrations.domain.User;
import com.event.management.registrations.repositories.EventsRepository;
import com.event.management.registrations.repositories.UsersRepository;
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
