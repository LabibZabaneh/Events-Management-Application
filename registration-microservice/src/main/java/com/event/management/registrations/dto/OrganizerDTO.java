package com.event.management.registrations.dto;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public class OrganizerDTO {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
