package com.event.management.events;

import com.event.management.events.domain.Event;
import com.event.management.events.dto.EventDTO;
import com.event.management.events.repositories.EventsRepository;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.event.management.events.UserControllerTest.iterableToList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@MicronautTest(transactional = false)
public class EventsControllerTest {

    @Inject
    EventsClient client;

    @Inject
    EventsRepository repo;

    @BeforeEach
    public void clean(){
        repo.deleteAll();
    }

    @Test
    public void noEvents(){
        Iterable<Event> events = client.list();
        assertFalse(events.iterator().hasNext(), "Service should not list any events initially");
    }

    @Test
    public void addEvent(){
        EventDTO dto = createEventDTO("Halloween", "York Parties", "Club Salvation", "31/10/2024", "23:00");

        HttpResponse<Void> resp = client.addEvent(dto);
        assertEquals(HttpStatus.CREATED, resp.getStatus(), "Creation should be successful");

        List<Event> events = iterableToList(repo.findAll());
        assertEquals(1, events.size(), "There should be only one event");

        compareEvents(events.get(0), dto);
    }

    @Test
    public void getEvent() {
        Event e = createEvent("Halloween", "York Parties", "Club Salvation", "31/10/2024", "23:00");
        repo.save(e);

        Event repoEvent = client.getEvent(e.getId());
        compareEvents(e, repoEvent);
    }

    @Test
    public void updateEvent(){
        Event e = createEvent("Halloween", "York Parties", "Club Salvation", "31/10/2024", "23:00");
        repo.save(e);

        EventDTO dto = createEventDTO("Freshers", "York Parties", "Revs", "25/09/2022", "12:00");
        HttpResponse<Void> resp = client.updateEvent(e.getId(), dto);
        assertEquals(HttpStatus.OK, resp.getStatus(), "Update should be successful");

        Event repoEvent = repo.findById(e.getId()).get();
        compareEvents(repoEvent, dto);
    }

    @Test
    public void deleteEvent() {
        Event e = createEvent("Halloween", "York Parties", "Club Salvation", "31/10/2024", "23:00");
        repo.save(e);

        HttpResponse<Void> resp = client.deleteEvent(e.getId());
        assertEquals(HttpStatus.OK, resp.getStatus(), "Deletion should be successful");
    }

    private Event createEvent(String eventName, String entityName, String venue, String date, String time) {
        Event e = new Event();
        e.setEventName(eventName);
        e.setEntityName(entityName);
        e.setVenue(venue);
        e.setDate(date);
        e.setTime(time);
        return e;
    }

    private EventDTO createEventDTO(String eventName, String entityName, String venue, String date, String time){
        EventDTO dto = new EventDTO();
        dto.setEventName(eventName);
        dto.setEntityName(entityName);
        dto.setVenue(venue);
        dto.setDate(date);
        dto.setTime(time);
        return dto;
    }

    private void compareEvents(Event e, EventDTO dto) {
        assertEquals(dto.getEventName(), e.getEventName(), "The event names should be the same");
        assertEquals(dto.getEntityName(), e.getEntityName(), "The entity names should be the same");
        assertEquals(dto.getVenue(), e.getVenue(), "The venues should be the same");
        assertEquals(dto.getDate(), e.getDate(), "The event dates should be the same");
        assertEquals(dto.getTime(), e.getTime(), "The event times should be the same");
    }

    private void compareEvents(Event e1, Event e2) {
        assertEquals(e1.getEventName(), e2.getEventName(), "The event names should be the same");
        assertEquals(e1.getEntityName(), e2.getEntityName(), "The entity names should be the same");
        assertEquals(e1.getVenue(), e2.getVenue(), "The venues should be the same");
        assertEquals(e1.getDate(), e2.getDate(), "The event dates should be the same");
        assertEquals(e1.getTime(), e2.getTime(), "The event times should be the same");
    }
}
