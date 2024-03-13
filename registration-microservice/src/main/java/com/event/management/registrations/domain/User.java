package com.event.management.registrations.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.micronaut.serde.annotation.Serdeable;

import javax.persistence.*;
import java.util.Set;

@Serdeable
@Entity
public class User {

    @Id
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String email;

    @JsonIgnore
    @ManyToMany(mappedBy = "registeredUsers")
    private Set<Event> registeredEvents;

    @JsonIgnore
    @ManyToMany(mappedBy = "followers")
    private Set<Organizer> followedOrganizers;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Event> getRegisteredEvents() {
        return registeredEvents;
    }

    public void setRegisteredEvents(Set<Event> registeredEvents) {
        this.registeredEvents = registeredEvents;
    }

    public Set<Organizer> getFollowedOrganizers() {
        return followedOrganizers;
    }

    public void setFollowedOrganizers(Set<Organizer> followedOrganizers) {
        this.followedOrganizers = followedOrganizers;
    }
}
