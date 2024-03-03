package com.event.management.registrations.dto;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public class EventDTO {

    private String eventName;

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

}
