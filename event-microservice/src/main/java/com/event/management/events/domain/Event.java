package com.event.management.events.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.micronaut.serde.annotation.Serdeable;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

@Serdeable
@Entity
public class Event {

    @GeneratedValue
    @Id
    private Long id;

    @Column(nullable = false)
    private String eventName;

    @ManyToOne
    @JoinColumn(name="organizer_id", nullable = false)
    private Organizer organizer;

    @Column(nullable = false)
    private String venue;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private LocalTime time;

    @JsonIgnore
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TicketCategory> ticketCategories;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Organizer getOrganizer() {
        return organizer;
    }

    public void setOrganizer(Organizer organizer) {
        this.organizer = organizer;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String name) {
        this.eventName = name;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
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

    public Set<TicketCategory> getTicketCategories() {
        return ticketCategories;
    }

    public void setTicketCategories(Set<TicketCategory> ticketCategories) {
        this.ticketCategories = ticketCategories;
    }
}
