package com.event.management.analytics.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.micronaut.serde.annotation.Serdeable;

import javax.persistence.*;
import java.util.Set;

@Serdeable
@Entity
public class Organizer {

    @Id
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private int followers;

    @JsonIgnore
    @OneToMany(mappedBy = "organizer", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Event> postedEvents;

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

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

    public void addFollower(){
        this.followers += 1;
    }

    public void removeFollower(){
        if (this.followers > 0){
            this.followers -= 1;
        }
    }

    public Set<Event> getPostedEvents() {
        return postedEvents;
    }

    public void setPostedEvents(Set<Event> postedEvents) {
        this.postedEvents = postedEvents;
    }
}
