package com.event.management.analytics.dto;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public class UserDTO {

    private String firstName;
    private int registrations;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public int getRegistrations() {
        return registrations;
    }

    public void setRegistrations(int registrations) {
        this.registrations = registrations;
    }
}
