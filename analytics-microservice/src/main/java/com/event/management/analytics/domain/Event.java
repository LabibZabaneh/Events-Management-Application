package com.event.management.analytics.domain;

import io.micronaut.serde.annotation.Serdeable;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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
    private double averageAge = 0;

    @OneToMany(mappedBy = "entity", cascade = CascadeType.ALL)
    private List<AgeCount> ageCounts = new ArrayList<>();

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

    public void addRegistration(int age){
        for (AgeCount ageCount : ageCounts){
            if (ageCount.getAge() == age){
                ageCount.incrementCount();
                this.registrations++;
                return;
            }
        }
        ageCounts.add(new AgeCount(this, age, 1));
        this.registrations++;
        this.averageAge = (this.averageAge * (this.registrations-1) + age) / (this.registrations);
    }

    public void deleteRegistration(){
        if (this.registrations > 0){
            this.registrations--;
        }
    }

    public Organizer getOrganizer() {
        return organizer;
    }

    public void setOrganizer(Organizer organizer) {
        this.organizer = organizer;
    }

    public List<AgeCount> getAgeCounts() {
        return ageCounts;
    }

    public void setAgeCounts(List<AgeCount> ageCounts) {
        this.ageCounts = ageCounts;
    }

    public double getAverageAge(){
        return averageAge;
    }
}
