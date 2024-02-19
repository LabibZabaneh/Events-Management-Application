package com.event.management.events.controllers;

import com.event.management.events.domain.Business;
import com.event.management.events.dto.BusinessDTO;
import com.event.management.events.repositories.BusinessRepository;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import jakarta.inject.Inject;

import javax.transaction.Transactional;
import java.net.URI;
import java.util.HashSet;
import java.util.Optional;

@Controller("/business")
public class BusinessController {

    @Inject
    BusinessRepository repo;

    @Get("/")
    public Iterable<Business> list(){
        return repo.findAll();
    }

    @Post("/")
    public HttpResponse<Void> addBusiness(@Body BusinessDTO dto){
        Business b = new Business();
        b.setName(dto.getName());
        b.setEmail(dto.getEmail());
        b.setPostedEvents(new HashSet<>());
        repo.save(b);

        return HttpResponse.created(URI.create("/business/" + b.getId()));
    }

    @Get("/{id}")
    public Business getBusiness(long id) {
        return repo.findById(id).orElse(null);
    }

    @Transactional
    @Put("/{id}")
    public HttpResponse<Void> updateBusiness(long id, @Body BusinessDTO dto){
        Optional<Business> oBusiness = repo.findById(id);
        if (oBusiness.isEmpty()){
            return HttpResponse.notFound();
        }
        Business b = oBusiness.get();
        if (dto.getName() != null){
            b.setName(dto.getName());
        }
        if (dto.getEmail() != null){
            b.setEmail(dto.getEmail());
        }
        repo.update(b);
        return HttpResponse.ok();
    }

    @Transactional
    @Delete("/{id}")
    public HttpResponse<Void> deleteBusiness(long id){
        Optional<Business> oBusiness = repo.findById(id);
        if (oBusiness.isEmpty()){
            return HttpResponse.notFound();
        }
        repo.delete(oBusiness.get());
        return HttpResponse.ok();
    }
}
