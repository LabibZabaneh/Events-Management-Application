package com.event.management.analytics.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.micronaut.serde.annotation.Serdeable;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
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

    @Column(nullable = false)
    private double averageAge;

    @JsonIgnore
    @OneToMany(mappedBy = "organizer", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Event> postedEvents;

    @OneToMany(mappedBy = "entity", cascade = CascadeType.ALL)
    private List<OrganizerAgeCount> ageCounts = new ArrayList<>(); // Regarding followers only

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

    public double getAverageAge() {
        return averageAge;
    }

    public void setAverageAge(double averageAge) {
        this.averageAge = averageAge;
    }

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

    public void addFollower(int age){
        for (OrganizerAgeCount ageCount : ageCounts){
            if (ageCount.getAge() == age){
                ageCount.incrementCount();
                this.followers++;
                return;
            }
        }
        ageCounts.add(new OrganizerAgeCount(this, age, 1));
        this.averageAge = (this.averageAge * (this.followers-1) + age) / this.followers;
        this.followers++;
    }

    public void removeFollower(int age){
        if (this.followers > 0 && this.averageAge > 0){
            for (OrganizerAgeCount ageCount : ageCounts){
                if (ageCount.getAge() == age){
                    ageCount.decrementCount();
                    this.followers--;
                    this.averageAge = (this.averageAge * (this.followers+1) - age) / this.followers;
                    return;
                }
            }
        }
    }

    public Set<Event> getPostedEvents() {
        return postedEvents;
    }

    public void setPostedEvents(Set<Event> postedEvents) {
        this.postedEvents = postedEvents;
    }

    public List<OrganizerAgeCount> getAgeCounts() {
        return ageCounts;
    }

    public void setAgeCounts(List<OrganizerAgeCount> ageCounts) {
        this.ageCounts = ageCounts;
    }
}
