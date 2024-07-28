package com.nadaveliash.trip_planner.model;

import java.util.List;

public class Track {
    private List<Landmark> landmarks;
    private List<String> routes;

    public Track() {
    }

    public Track(List<Landmark> landmarks, List<String> routes) {
        this.landmarks = landmarks;
        this.routes = routes;
    }

    public List<Landmark> getLandmarks() {
        return landmarks;
    }

    public void setLandmarks(List<Landmark> landmarks) {
        this.landmarks = landmarks;
    }

    public List<String> getRoutes() {
        return routes;
    }

    public void setRoutes(List<String> routes) {
        this.routes = routes;
    }

    @Override
    public String toString() {
        return "{" +
                "\"landmarks\": \"" + landmarks + "\"," +
                "\"routes\": \"" + routes + "\"" +
                "}";
    }
}
