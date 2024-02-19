package com.event.management.events.dto;

import com.event.management.events.domain.Business;
import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public class EventDTO {

    private String eventName;
    private Long businessId;
    private String venue;
    private String date;
    private String time;

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String name) {
        this.eventName = name;
    }

    public Long getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Long businessId) {
        this.businessId = businessId;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}
