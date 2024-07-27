package com.nadaveliash.trip_planner.model;

public class Landmark {
    private String destination;
    private String day;

    public Landmark() {
    }

    public Landmark(String destination, String day) {
        this.destination = destination;
        this.day = day;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }
}
