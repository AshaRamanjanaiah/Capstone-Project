package com.example.android.hindutemple.model;

public class Timings {
    private String timingsId;
    private String timingsDay;
    private String timingsOpen;
    private String timingsClose;

    public Timings(){

    }

    public Timings(String timingsId, String timingsDay, String timingsOpen, String timingsClose) {
        this.timingsId = timingsId;
        this.timingsDay = timingsDay;
        this.timingsOpen = timingsOpen;
        this.timingsClose = timingsClose;
    }

    public String getTimingsId() {
        return timingsId;
    }

    public String getTimingsDay() {
        return timingsDay;
    }

    public String getTimingsOpen() {
        return timingsOpen;
    }

    public String getTimingsClose() {
        return timingsClose;
    }
}
