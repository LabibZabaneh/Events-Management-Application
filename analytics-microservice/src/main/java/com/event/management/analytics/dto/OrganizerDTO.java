package com.event.management.analytics.dto;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public class OrganizerDTO {

    private String name;
    private String followers;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFollowers() {
        return followers;
    }

    public void setFollowers(String followers) {
        this.followers = followers;
    }
}
