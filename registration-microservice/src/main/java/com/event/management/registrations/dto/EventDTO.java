package com.event.management.registrations.dto;

import com.event.management.registrations.domain.TicketCategory;
import io.micronaut.serde.annotation.Serdeable;

import java.util.List;

@Serdeable
public class EventDTO {

    private String eventName;

    private List<TicketCategory>  ticketCategories;

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public List<TicketCategory> getTicketCategories() {
        return ticketCategories;
    }

    public void setTicketCategories(List<TicketCategory> ticketCategories) {
        this.ticketCategories = ticketCategories;
    }
}
