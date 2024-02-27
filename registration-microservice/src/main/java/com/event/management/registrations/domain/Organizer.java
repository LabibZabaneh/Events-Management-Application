package com.event.management.registrations.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.micronaut.serde.annotation.Serdeable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.Set;

@Serdeable
@Entity
public class Organizer {

    @Id
    private Long id;

    @Column(nullable = false)
    private String name;

    @JsonIgnore
    @ManyToMany
    private Set<User> followers;

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

    public Set<User> getFollowers() {
        return followers;
    }

    public void setFollowers(Set<User> followers) {
        this.followers = followers;
    }
}
