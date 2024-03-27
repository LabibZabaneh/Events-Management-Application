package com.event.management.events.domain;

import io.micronaut.serde.annotation.Serdeable;

import javax.persistence.*;

@Serdeable
@Embeddable
public class TicketCategory {

    private String name;
    private int initialCount;
    private double price;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getInitialCount() {
        return initialCount;
    }

    public void setInitialCount(int initialCount) {
        this.initialCount = initialCount;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

}

