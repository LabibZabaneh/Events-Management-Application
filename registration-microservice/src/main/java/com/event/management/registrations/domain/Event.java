package com.event.management.registrations.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.micronaut.serde.annotation.Serdeable;

import javax.persistence.*;
import java.util.Set;

@Serdeable
@Entity
public class Event {

    @Id
    private Long id;

    @Column(nullable = false)
    private String eventName;

    @JsonIgnore
    @ManyToMany
    private Set<User> registeredUsers;

    @JsonIgnore
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TicketCategory> ticketCategories;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public Set<User> getRegisteredUsers() {
        return registeredUsers;
    }

    public void setRegisteredUsers(Set<User> registeredUsers) {
        this.registeredUsers = registeredUsers;
    }

    public Set<TicketCategory> getTicketCategories() {
        return ticketCategories;
    }

    public void setTicketCategories(Set<TicketCategory> ticketCategories) {
        this.ticketCategories = ticketCategories;
    }
}
