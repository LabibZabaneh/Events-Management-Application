package com.event.management.registrations.domain;

import io.micronaut.serde.annotation.Serdeable;

import javax.persistence.*;

@Serdeable
@Embeddable
public class TicketCategory {

    private String name;
    private int totalTicketCount;
    private int soldTicketsCount;
    private double price;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTotalTicketCount() {
        return totalTicketCount;
    }

    public void setTotalTicketCount(int totalTicketCount) {
        totalTicketCount = totalTicketCount;
    }

    public int getSoldTicketsCount() {
        return soldTicketsCount;
    }

    public void setSoldTicketsCount(int soldTicketsCount) {
        this.soldTicketsCount = soldTicketsCount;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void incrementSoldTicketCount(){
        soldTicketsCount++;
    }

    public void decrementSoldTicketCount(){
        if (soldTicketsCount > 0){
            soldTicketsCount--;
        }
    }

}
