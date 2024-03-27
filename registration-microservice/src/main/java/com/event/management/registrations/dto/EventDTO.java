package com.event.management.registrations.dto;

import io.micronaut.serde.annotation.Serdeable;

import java.util.List;

@Serdeable
public class EventDTO {

    private String eventName;

    private List<TicketCategoryDTO>  ticketCategories;

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public List<TicketCategoryDTO> getTicketCategories() {
        return ticketCategories;
    }

    public void setTicketCategories(List<TicketCategoryDTO> ticketCategories) {
        this.ticketCategories = ticketCategories;
    }
}
