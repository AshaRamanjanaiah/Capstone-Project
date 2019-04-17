package com.example.android.hindutemple.model;

public class Events {
    private String eventId;
    private String eventName;
    private String eventDate;
    private String eventTime;

    public Events(){

    }

    public Events(String eventId, String eventName, String eventDate, String eventTime) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.eventTime = eventTime;
    }

    public String getEventId() {
        return eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public String getEventDate() {
        return eventDate;
    }

    public String getEventTime() {
        return eventTime;
    }
}
