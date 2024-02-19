package com.event.management.events.dto;

import com.event.management.events.domain.Event;

import io.micronaut.serde.annotation.Serdeable;

import java.util.Set;

@Serdeable
public class BusinessDTO {

    private String name;
    private String email;
    private Set<Event> postedEvents;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Event> getPostedEvents() {
        return postedEvents;
    }

    public void setPostedEvents(Set<Event> postedEvents) {
        this.postedEvents = postedEvents;
    }
}
