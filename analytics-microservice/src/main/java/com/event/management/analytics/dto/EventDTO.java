package com.event.management.analytics.dto;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public class EventDTO {

    private String name;
    private int registrations;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRegistrations() {
        return registrations;
    }

    public void setRegistrations(int registrations) {
        this.registrations = registrations;
    }
}
