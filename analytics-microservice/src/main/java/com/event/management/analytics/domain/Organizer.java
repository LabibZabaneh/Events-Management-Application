package com.event.management.analytics.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.micronaut.serde.annotation.Serdeable;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
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

    @Column(nullable = false)
    private int maleFollowers;

    @Column(nullable = false)
    private int femaleFollowers;

    @Column(nullable = false)
    private int otherFollowers;

    @JsonIgnore
    @OneToMany(mappedBy = "organizer", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Event> postedEvents;

    @ElementCollection
    private Set<AgeCount> ageCounts = new HashSet<>(); // Regarding followers only

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

    public void addFollower(int age, Gender gender){
        for (AgeCount ageCount : ageCounts){
            if (ageCount.getAge() == age){
                ageCount.incrementCount();
                incrementGenderCount(gender);
                this.followers++;
                return;
            }
        }
        ageCounts.add(new AgeCount(age, 1));
        this.averageAge = (this.averageAge * (this.followers-1) + age) / this.followers;
        incrementGenderCount(gender);
        this.followers++;
    }

    public void removeFollower(int age, Gender gender){
        if (this.followers>0 && this.averageAge>0 && maleFollowers>0 && femaleFollowers>0 && otherFollowers>0){
            for (AgeCount ageCount : ageCounts){
                if (ageCount.getAge() == age){
                    ageCount.decrementCount();
                    this.followers--;
                    decrementGenderCount(gender);
                    this.averageAge = (this.averageAge * (this.followers+1) - age) / this.followers;
                    return;
                }
            }
        }
    }

    public int getMaleFollowers() {
        return maleFollowers;
    }

    public void setMaleFollowers(int maleFollowers) {
        this.maleFollowers = maleFollowers;
    }

    public int getFemaleFollowers() {
        return femaleFollowers;
    }

    public void setFemaleFollowers(int femaleFollowers) {
        this.femaleFollowers = femaleFollowers;
    }

    public int getOtherFollowers() {
        return otherFollowers;
    }

    public void setOtherFollowers(int otherFollowers) {
        this.otherFollowers = otherFollowers;
    }

    public Set<Event> getPostedEvents() {
        return postedEvents;
    }

    public void setPostedEvents(Set<Event> postedEvents) {
        this.postedEvents = postedEvents;
    }

    public Set<AgeCount> getAgeCounts() {
        return ageCounts;
    }

    public void setAgeCounts(Set<AgeCount> ageCounts) {
        this.ageCounts = ageCounts;
    }

    private void incrementGenderCount(Gender gender){
        switch (gender) {
            case MALE:
                maleFollowers++;
            case FEMALE:
                femaleFollowers++;
                break;
            case OTHER:
                otherFollowers++;
                break;
        }
    }

    private void decrementGenderCount(Gender gender){ // Checked for 0 values before calling the method
        switch (gender) {
            case MALE:
                maleFollowers--;
            case FEMALE:
                femaleFollowers--;
                break;
            case OTHER:
                otherFollowers--;
                break;
        }
    }
}
