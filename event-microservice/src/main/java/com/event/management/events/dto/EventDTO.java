package com.event.management.events.dto;

import com.event.management.events.domain.TicketCategory;
import io.micronaut.serde.annotation.Serdeable;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Serdeable
public class EventDTO {

    private String eventName;
    private Long organizerId;
    private String venue;
    private LocalDate date;
    private LocalTime time;
    private List<TicketCategory> ticketCategories;

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String name) {
        this.eventName = name;
    }

    public Long getOrganizerId() {
        return organizerId;
    }

    public void setOrganizerId(Long organizerId) {
        this.organizerId = organizerId;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public List<TicketCategory> getTicketCategories() {
        return ticketCategories;
    }

    public void setTicketCategories(List<TicketCategory> ticketCategories) {
        this.ticketCategories = ticketCategories;
    }
}
