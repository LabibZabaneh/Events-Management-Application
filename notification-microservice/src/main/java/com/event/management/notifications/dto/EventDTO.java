package com.event.management.notifications.dto;

import io.micronaut.serde.annotation.Serdeable;

import javax.persistence.Column;
import java.time.LocalDate;
import java.time.LocalTime;

@Serdeable
public class EventDTO {

    private String name;
    private LocalDate date;
    private LocalTime time;
    private String venue;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }
}
