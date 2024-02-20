package com.event.management.events;

import com.event.management.events.domain.Business;
import com.event.management.events.dto.BusinessDTO;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.http.client.annotation.Client;

@Client("${business.url:`http://localhost:8080/business`}")
public interface BusinessClient {

    @Get("/")
    Iterable<Business> list();

    @Post("/")
    HttpResponse<Void> addBusiness(@Body BusinessDTO dto);

    @Get("/{id}")
    Business getBusiness(long id);

    @Put("/{id}")
    HttpResponse<Void> updateBusiness(long id, @Body BusinessDTO dto);

    @Delete("/{id}")
    HttpResponse<Void> deleteBusiness(long id);
}
