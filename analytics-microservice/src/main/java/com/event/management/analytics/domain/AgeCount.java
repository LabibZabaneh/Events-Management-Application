package com.event.management.analytics.domain;


import io.micronaut.serde.annotation.Serdeable;

import javax.persistence.*;

@Serdeable
@Entity
public class AgeCount {

    @GeneratedValue
    @Id
    private Long id;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name="event_id")
    private Event event;

    @Column(nullable = false)
    private int age;

    @Column(nullable = false)
    private int count;

    public AgeCount(Event event, int age, int count) {
        this.event = event;
        this.age = age;
        this.count = 0;
        if (count < 0){
            this.count = count;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
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
