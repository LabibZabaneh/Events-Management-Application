package com.event.management.analytics.domain;

import io.micronaut.serde.annotation.Serdeable;

import javax.persistence.*;

@Serdeable
@Entity
public class Event {

    @Id
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name="organizer_id", nullable = false)
    private Organizer organizer;

    @Column(nullable = false)
    private int registrations;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRegistrations() {
        return registrations;
    }

    public void setRegistrations(int registrations){
        this.registrations = registrations;
    }

    public void addRegistration(){
        this.registrations += 1;
    }

    public void deleteRegistration(){
        if (this.registrations > 0){
            this.registrations -= 1;
        }
    }

    public Organizer getOrganizer() {
        return organizer;
    }

    public void setOrganizer(Organizer organizer) {
        this.organizer = organizer;
    }
}
