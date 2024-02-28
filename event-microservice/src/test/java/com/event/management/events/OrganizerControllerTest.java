package com.event.management.events;

import com.event.management.events.clients.OrganizerClient;
import com.event.management.events.domain.Organizer;
import com.event.management.events.dto.OrganizerDTO;
import com.event.management.events.repositories.OrganizersRepository;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;

import static com.event.management.events.UserControllerTest.iterableToList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@MicronautTest(transactional = false)
public class OrganizerControllerTest {

    @Inject
    OrganizerClient client;

    @Inject
    OrganizersRepository repo;

    @BeforeEach
    public void clean(){
        repo.deleteAll();
    }

    @Test
    public void noOrganizers(){
        Iterable<Organizer> organizers = client.list();
        assertFalse(organizers.iterator().hasNext(), "Service should not list any organizers initially");
    }

    @Test
    public void addOrganizers(){
        OrganizerDTO dto = createOrganizerDTO("York Parties", "york.parties@gmail.com");

        HttpResponse<Void> resp = client.addOrganizer(dto);
        assertEquals(HttpStatus.CREATED, resp.getStatus(), "Creation should be successful");

        List<Organizer> organizers =  iterableToList(repo.findAll());
        assertEquals(1, organizers.size(), "There should only be one total organizer");

        Organizer o = organizers.get(0);
        assertEquals(dto.getName(), o.getName(), "The names should be the same");
        assertEquals(dto.getEmail(), o.getEmail(), "The emails should be the same");
    }

    @Test
    public void getOrganizer(){
        Organizer o = createOrganizer("York Parties", "york.parties@gmail.com");
        repo.save(o);

        Organizer repoOrganizer = client.getOrganizer(o.getId());
        assertEquals(o.getName(), repoOrganizer.getName(), "The names should be the same");
        assertEquals(o.getEmail(), repoOrganizer.getEmail(), "The emails should be the same");
    }

    @Test
    public void updateOrganizer(){
        Organizer o = createOrganizer("York Parties", "york.parties@gmail.com");
        repo.save(o);

        OrganizerDTO dto = createOrganizerDTO("Leeds Parties", "leeds.parties@gmail.com");

        HttpResponse<Void> resp = client.updateOrganizer(o.getId(), dto);
        assertEquals(HttpStatus.OK, resp.getStatus(), "Update should be successful");

        Organizer repoOrganizer = repo.findById(o.getId()).get();
        assertEquals(dto.getName(), repoOrganizer.getName(), "The names should be the same");
        assertEquals(dto.getEmail(), repoOrganizer.getEmail(), "The emails should be the same");
    }

    @Test
    public void deleteOrganizer(){
        Organizer o = createOrganizer("York Parties", "york.parties@gmail.com");
        repo.save(o);

        HttpResponse<Void> resp = client.deleteOrganizer(o.getId());
        assertEquals(HttpStatus.OK, resp.getStatus(), "Deletion should be successful");

        assertFalse(repo.existsById(o.getId()));
    }

    private OrganizerDTO createOrganizerDTO(String name, String email){
        OrganizerDTO dto = new OrganizerDTO();
        dto.setName(name);
        dto.setEmail(email);
        return dto;
    }

    protected static Organizer createOrganizer(String name, String email){
        Organizer o = new Organizer();
        o.setEmail(email);
        o.setName(name);
        o.setPostedEvents(new HashSet<>());
        return o;
    }
}
