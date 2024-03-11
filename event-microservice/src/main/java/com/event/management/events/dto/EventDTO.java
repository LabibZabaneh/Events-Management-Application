package com.event.management.events.dto;

import io.micronaut.serde.annotation.Serdeable;

import java.time.LocalDate;
import java.time.LocalTime;

@Serdeable
public class EventDTO {

    private String eventName;
    private Long organizerId;
    private String venue;
    private LocalDate date;
    private LocalTime time;

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
}
