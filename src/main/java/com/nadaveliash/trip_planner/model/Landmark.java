package com.nadaveliash.trip_planner.model;

public class Landmark {
    private String destination;
    private Double lat;
    private Double lng;
    private String day;

    public Landmark() {
    }

    public Landmark(String destination, Double lat, Double lng, String day) {
        this.destination = destination;
        this.lat = lat;
        this.lng = lng;
        this.day = day;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }


    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    @Override
    public String toString() {
        return "{" +
                "\"destination\":\"" + destination + "\"," +
                "\"lat\":\"" + lat + "\"," +
                "\"lng\":\"" + lng + "\"," +
                "\"day\": \"" + day + "\"" +
                "}";
    }
}