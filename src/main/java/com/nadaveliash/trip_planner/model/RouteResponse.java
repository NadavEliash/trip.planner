package com.nadaveliash.trip_planner.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RouteResponse {
    @JsonProperty("distanceMeters")
    private int distanceMeters;

    @JsonProperty("duration")
    private String duration;

    @JsonProperty("polyline")
    private Polyline polyline;

    public int getDistanceMeters() {
        return distanceMeters;
    }

    public void setDistanceMeters(int distanceMeters) {
        this.distanceMeters = distanceMeters;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Polyline getPolyline() {
        return polyline;
    }

    public void setPolyline(Polyline polyline) {
        this.polyline = polyline;
    }

    @Override
    public String toString() {
        return "{" +
                "distanceMeters=" + distanceMeters +
                ", duration='" + duration + '\'' +
                ", polyline=" + polyline +
                '}';
    }

    public class Polyline {

        @JsonProperty("encodedPolyline")
        private String encodedPolyline;

        public String getEncodedPolyline() {
            return encodedPolyline;
        }

        public void setEncodedPolyline(String encodedPolyline) {
            this.encodedPolyline = encodedPolyline;
        }

        @Override
        public String toString() {
            return encodedPolyline;
        }
    }
}
