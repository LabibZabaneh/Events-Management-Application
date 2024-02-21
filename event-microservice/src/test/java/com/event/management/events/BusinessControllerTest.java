package com.event.management.events;

import com.event.management.events.clients.BusinessClient;
import com.event.management.events.domain.Business;
import com.event.management.events.dto.BusinessDTO;
import com.event.management.events.repositories.BusinessRepository;
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
public class BusinessControllerTest {

    @Inject
    BusinessClient client;

    @Inject
    BusinessRepository repo;

    @BeforeEach
    public void clean(){
        repo.deleteAll();
    }

    @Test
    public void noBusinesses(){
        Iterable<Business> businesses = repo.findAll();
        assertFalse(businesses.iterator().hasNext(), "Service should not list any events initially");
    }

    @Test
    public void addBusiness(){
        BusinessDTO dto = createBusinessDTO("York Parties", "york.parties@gmail.com");

        HttpResponse<Void> resp = client.addBusiness(dto);
        assertEquals(HttpStatus.CREATED, resp.getStatus(), "Creation should be successful");

        List<Business> businesses =  iterableToList(repo.findAll());
        assertEquals(1, businesses.size(), "There should only be one total business");

        Business b = businesses.get(0);
        assertEquals(dto.getName(), b.getName(), "The names should be the same");
        assertEquals(dto.getEmail(), b.getEmail(), "The emails should be the same");
    }

    @Test
    public void getBusiness(){
        Business b = createBusiness("York Parties", "york.parties@gmail.com");
        repo.save(b);

        Business repoBusiness = client.getBusiness(b.getId());
        assertEquals(b.getName(), repoBusiness.getName(), "The names should be the same");
        assertEquals(b.getEmail(), repoBusiness.getEmail(), "The emails should be the same");
    }

    @Test
    public void updateBusiness(){
        Business b = createBusiness("York Parties", "york.parties@gmail.com");
        repo.save(b);

        BusinessDTO dto = createBusinessDTO("Leeds Parties", "leeds.parties@gmail.com");

        HttpResponse<Void> resp = client.updateBusiness(b.getId(), dto);
        assertEquals(HttpStatus.OK, resp.getStatus(), "Update should be successful");

        Business repoBusiness = repo.findById(b.getId()).get();
        assertEquals(dto.getName(), repoBusiness.getName(), "The names should be the same");
        assertEquals(dto.getEmail(), repoBusiness.getEmail(), "The emails should be the same");
    }

    @Test
    public void deleteBusiness(){
        Business b = createBusiness("York Parties", "york.parties@gmail.com");
        repo.save(b);

        HttpResponse<Void> resp = client.deleteBusiness(b.getId());
        assertEquals(HttpStatus.OK, resp.getStatus(), "Deletion should be successful");

        assertFalse(repo.existsById(b.getId()));
    }

    private BusinessDTO createBusinessDTO(String name, String email){
        BusinessDTO dto = new BusinessDTO();
        dto.setName(name);
        dto.setEmail(email);
        return dto;
    }

    private Business createBusiness(String name, String email){
        Business b = new Business();
        b.setEmail(email);
        b.setName(name);
        b.setPostedEvents(new HashSet<>());
        return b;
    }
}
