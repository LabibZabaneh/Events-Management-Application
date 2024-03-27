package com.event.management.analytics.domain;

import io.micronaut.serde.annotation.Serdeable;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    @Column(nullable = false)
    private double averageAge;
    @Column(nullable = false)
    private int maleRegistrations;
    @Column(nullable = false)
    private int femaleRegistrations;
    @Column(nullable = false)
    private int otherRegistrations;

    @ElementCollection
    private Set<AgeCount> ageCounts = new HashSet<>();


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

    public void addRegistration(int age, Gender gender){
        boolean found = false;
        for (AgeCount ageCount : ageCounts){
            if (ageCount.getAge() == age){
                ageCount.incrementCount();
                found = true;
                break;
            }
        }
        if (!found) {
            ageCounts.add(new AgeCount(age, 1));
        }
        this.registrations++;
        incrementGenderCount(gender);
        this.averageAge = (this.averageAge * (this.registrations-1) + age) / this.registrations;
    }

    public void deleteRegistration(int age, Gender gender) {
        if (this.registrations<=0 || this.averageAge<=0 || maleRegistrations<=0 || femaleRegistrations<=0 || otherRegistrations<=0) {
            return;
        }

        for (AgeCount ageCount : ageCounts) {
            if (ageCount.getAge() == age) {
                ageCount.decrementCount();
                this.registrations--;
                decrementGenderCount(gender);
                // Update average age
                if (this.registrations > 0) {
                    this.averageAge = ((this.averageAge * (this.registrations + 1)) - age) / this.registrations;
                } else {
                    this.averageAge = 0; // Reset average age if no registrations
                }
                return;
            }
        }
    }

    private void incrementGenderCount(Gender gender){
        switch (gender) {
            case MALE:
                maleRegistrations++;
            case FEMALE:
                femaleRegistrations++;
                break;
            case OTHER:
                otherRegistrations++;
                break;
        }
    }

    private void decrementGenderCount(Gender gender){ // Checked for 0 values before calling the method
        switch (gender) {
            case MALE:
                maleRegistrations--;
            case FEMALE:
                femaleRegistrations--;
                break;
            case OTHER:
                otherRegistrations--;
                break;
        }
    }

    public Organizer getOrganizer() {
        return organizer;
    }

    public void setOrganizer(Organizer organizer) {
        this.organizer = organizer;
    }

    public Set<AgeCount> getAgeCounts() {
        return ageCounts;
    }

    public void setAgeCounts(Set<AgeCount> ageCounts) {
        this.ageCounts = ageCounts;
    }

    public double getAverageAge(){
        return averageAge;
    }

    public void setAverageAge(double averageAge) {
        this.averageAge = averageAge;
    }

    public int getMaleRegistrations() {
        return maleRegistrations;
    }

    public void setMaleRegistrations(int maleRegistrations) {
        this.maleRegistrations = maleRegistrations;
    }

    public int getFemaleRegistrations() {
        return femaleRegistrations;
    }

    public void setFemaleRegistrations(int femaleRegistrations) {
        this.femaleRegistrations = femaleRegistrations;
    }

    public int getOtherRegistrations() {
        return otherRegistrations;
    }

    public void setOtherRegistrations(int otherRegistrations) {
        this.otherRegistrations = otherRegistrations;
    }
}
