package com.event.management.analytics.domain;

import io.micronaut.serde.annotation.Serdeable;

import javax.persistence.Embeddable;

@Serdeable
@Embeddable
public class AgeCount {

    private int age;
    private int count;

    public AgeCount() {}

    AgeCount(int age, int count){
        this.age = age;
        this.count = count;
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
