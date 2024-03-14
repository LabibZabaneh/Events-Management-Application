package com.event.management.analytics.domain;

import io.micronaut.serde.annotation.Serdeable;

import javax.persistence.*;

@Serdeable
@Entity
public class OrganizerAgeCount {

    @GeneratedValue
    @Id
    private Long id;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name="organizer_id")
    private Organizer organizer;

    @Column(nullable = false)
    private int age;

    @Column(nullable = false)
    private int count;

    public OrganizerAgeCount(Organizer organizer, int age, int count) {
        this.organizer = organizer;
        this.age = age;
        this.count = 0;
        if (count < 0){
            this.count = count;
        }
    }

    public OrganizerAgeCount() {

    }

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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void incrementCount(){
        this.count++;
    }

    public void decrementCount(){
        if (this.count > 0){
            this.count--;
        }
    }
}
