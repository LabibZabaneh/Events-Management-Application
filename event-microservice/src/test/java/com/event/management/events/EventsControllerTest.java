package com.event.management.events;

import com.event.management.events.clients.EventsClient;
import com.event.management.events.domain.Event;
import com.event.management.events.domain.Organizer;
import com.event.management.events.dto.EventDTO;
import com.event.management.events.repositories.EventsRepository;
import com.event.management.events.repositories.OrganizersRepository;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.event.management.events.OrganizerControllerTest.createOrganizer;
import static com.event.management.events.UserControllerTest.iterableToList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@MicronautTest(transactional = false)
public class EventsControllerTest {

    @Inject
    EventsClient client;

    @Inject
    EventsRepository repo;

    @Inject
    OrganizersRepository organizerRepo;

    @BeforeEach
    public void clean(){
        repo.deleteAll();
        organizerRepo.deleteAll();
    }

    @Test
    public void noEvents(){
        Iterable<Event> events = client.list();
        assertFalse(events.iterator().hasNext(), "Service should not list any events initially");
    }

    @Test
    public void addEventWithInvalidOrganizer(){
        EventDTO dto = createEventDTO("Halloween", 0L, "Club Salvation", "31/10/2024", "23:00");

        HttpResponse<Void> resp = client.addEvent(dto);
        assertEquals(HttpStatus.NOT_FOUND, resp.getStatus(), "Creation should not be successful");
    }

    @Test
    public void addEvent(){
        Organizer o = createOrganizer("York Parties", "york.parties@gmail.com");
        organizerRepo.save(o);

        EventDTO dto = createEventDTO("Halloween", o.getId(), "Club Salvation", "31/10/2024", "23:00");

        HttpResponse<Void> resp = client.addEvent(dto);
        assertEquals(HttpStatus.CREATED, resp.getStatus(), "Creation should be successful");

        List<Event> events = iterableToList(repo.findAll());
        assertEquals(1, events.size(), "There should be only one event");

        compareEvents(events.get(0), dto);
    }

    @Test
    public void getEvent() {
        Organizer o = createOrganizer("York Parties", "york.parties@gmail.com");
        organizerRepo.save(o);

        Event e = createEvent("Halloween", o, "Club Salvation", "31/10/2024", "23:00");
        repo.save(e);

        Event repoEvent = client.getEvent(e.getId());
        compareEvents(e, repoEvent);
    }

    @Test
    public void updateEvent(){
        Organizer o = createOrganizer("York Parties", "york.parties@gmail.com");
        organizerRepo.save(o);

        Event e = createEvent("Halloween", o, "Club Salvation", "31/10/2024", "23:00");
        repo.save(e);

        EventDTO dto = createEventDTO("Freshers", o.getId(), "Revs", "25/09/2022", "12:00");
        HttpResponse<Void> resp = client.updateEvent(e.getId(), dto);
        assertEquals(HttpStatus.OK, resp.getStatus(), "Update should be successful");

        Event repoEvent = repo.findById(e.getId()).get();
        compareEvents(repoEvent, dto);
    }

    @Test
    public void deleteEvent() {
        Organizer o = createOrganizer("York Parties", "york.parties@gmail.com");
        organizerRepo.save(o);

        Event e = createEvent("Halloween", o, "Club Salvation", "31/10/2024", "23:00");
        repo.save(e);

        HttpResponse<Void> resp = client.deleteEvent(e.getId());
        assertEquals(HttpStatus.OK, resp.getStatus(), "Deletion should be successful");
    }

    private Event createEvent(String eventName, Organizer organizer, String venue, String date, String time) {
        Event e = new Event();
        e.setEventName(eventName);
        e.setOrganizer(organizer);
        e.setVenue(venue);
        e.setDate(date);
        e.setTime(time);
        return e;
    }

    private EventDTO createEventDTO(String eventName, Long organizerId, String venue, String date, String time){
        EventDTO dto = new EventDTO();
        dto.setEventName(eventName);
        dto.setOrganizerId(organizerId);
        dto.setVenue(venue);
        dto.setDate(date);
        dto.setTime(time);
        return dto;
    }

    private void compareEvents(Event e, EventDTO dto) {
        assertEquals(dto.getEventName(), e.getEventName(), "The event names should be the same");
        assertEquals(dto.getOrganizerId(), e.getOrganizer().getId(), "The organizers should be the same");
        assertEquals(dto.getVenue(), e.getVenue(), "The venues should be the same");
        assertEquals(dto.getDate(), e.getDate(), "The event dates should be the same");
        assertEquals(dto.getTime(), e.getTime(), "The event times should be the same");
    }

    private void compareEvents(Event e1, Event e2) {
        assertEquals(e1.getEventName(), e2.getEventName(), "The event names should be the same");
        assertEquals(e1.getOrganizer().getId(), e2.getOrganizer().getId(), "The organizers should be the same");
        assertEquals(e1.getVenue(), e2.getVenue(), "The venues should be the same");
        assertEquals(e1.getDate(), e2.getDate(), "The event dates should be the same");
        assertEquals(e1.getTime(), e2.getTime(), "The event times should be the same");
    }
}
