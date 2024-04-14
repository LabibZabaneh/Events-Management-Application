package com.event.management.notifications.dto;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public class UserDTO {

    private String fistName;
    private String email;

    public String getFistName() {
        return fistName;
    }

    public void setFistName(String fistName) {
        this.fistName = fistName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
