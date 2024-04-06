package com.event.management.registrations.domain;

import io.micronaut.serde.annotation.Serdeable;

import javax.persistence.*;
import java.util.List;

@Serdeable
@Entity
public class TicketCategory {

    @Id
    @GeneratedValue
    private String name;
    @Column(nullable = false)
    private double price;
    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;
    @OneToMany(mappedBy = "ticketCategory", cascade = CascadeType.ALL)
    private List<Ticket> reservedTickets;
    @OneToMany(mappedBy = "ticketCategory", cascade = CascadeType.ALL)
    private List<Ticket> soldTickets;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public List<Ticket> getReservedTickets() {
        return reservedTickets;
    }

    public void setReservedTickets(List<Ticket> reservedTickets) {
        this.reservedTickets = reservedTickets;
    }

    public List<Ticket> getSoldTickets() {
        return soldTickets;
    }

    public void setSoldTickets(List<Ticket> soldTickets) {
        this.soldTickets = soldTickets;
    }
}
