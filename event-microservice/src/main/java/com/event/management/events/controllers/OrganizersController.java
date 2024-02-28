package com.event.management.events.controllers;

import com.event.management.events.domain.Organizer;
import com.event.management.events.dto.OrganizerDTO;
import com.event.management.events.kafka.OrganizersProducer;
import com.event.management.events.repositories.OrganizersRepository;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import jakarta.inject.Inject;

import javax.transaction.Transactional;
import java.net.URI;
import java.util.HashSet;
import java.util.Optional;

@Controller("/organizers")
public class OrganizersController {

    @Inject
    OrganizersRepository repo;

    @Inject
    OrganizersProducer producer;

    @Get("/")
    public Iterable<Organizer> list(){
        return repo.findAll();
    }

    @Post("/")
    public HttpResponse<Void> addOrganizer(@Body OrganizerDTO dto){
        Organizer o = new Organizer();
        o.setName(dto.getName());
        o.setEmail(dto.getEmail());
        o.setPostedEvents(new HashSet<>());
        repo.save(o);

        producer.createdOrganizer(o.getId(), dto);

        return HttpResponse.created(URI.create("/organizer/" + o.getId()));
    }

    @Get("/{id}")
    public Organizer getOrganizer(long id) {
        return repo.findById(id).orElse(null);
    }

    @Transactional
    @Put("/{id}")
    public HttpResponse<Void> updateOrganizer(long id, @Body OrganizerDTO dto){
        Optional<Organizer> oOrganizer = repo.findById(id);
        if (oOrganizer.isEmpty()){
            return HttpResponse.notFound();
        }
        Organizer o = oOrganizer.get();
        if (dto.getName() != null){
            o.setName(dto.getName());
        }
        if (dto.getEmail() != null){
            o.setEmail(dto.getEmail());
        }
        repo.update(o);
        return HttpResponse.ok();
    }

    @Transactional
    @Delete("/{id}")
    public HttpResponse<Void> deleteOrganizer(long id){
        Optional<Organizer> oOrganizer = repo.findById(id);
        if (oOrganizer.isEmpty()){
            return HttpResponse.notFound();
        }
        Organizer o = oOrganizer.get();
        repo.delete(o);

        OrganizerDTO dto = new OrganizerDTO();
        dto.setEmail(o.getEmail());
        dto.setName(o.getName());
        producer.deletedOrganizer(id, dto);

        return HttpResponse.ok();
    }
}
