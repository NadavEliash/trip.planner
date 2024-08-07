package com.nadaveliash.trip_planner.model;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "trips")
@Getter
@Setter
public class TripDB {
    @Id
    private String id;
    private String destination;
    private String startDate;
    private String endDate;
    private String options;

    private Trip trip;
    private List<DayData> days;
    private List<Album> album;

    public TripDB() {}

    public TripDB(String destination, String startDate, String endDate, String options, Trip trip, List<DayData> days, List<Album> album) {
        this.destination = destination;
        this.startDate = startDate;
        this.endDate = endDate;
        this.options = options;
        this.trip = trip;
        this.days = days;
        this.album = album;
    }

    @Getter
    @Setter
    public static class DayData {
        private Day day;
        private DayTrip trip;
    }

    @Getter
    @Setter
    public static class Day {
        private String date;
        private List<Destination> destinations;
    }

    @Getter
    @Setter
    public static class Destination {
        private String address;
        private Double lat;
        private Double lng;
    }

    @Getter
    @Setter
    public static class DayTrip {
        private String description;
        private List<String> places;
        private List<String> recommendations;
    }

    @Getter
    @Setter
    public static class Album {
        private String name;
        private List<String> photos;
    }

    @Getter
    @Setter
    public static class Trip {
        private List<Landmark> landmarks;
        private List<String> routes;
    }

    @Getter
    @Setter
    public static class Landmark {
        private String destination;
        private Double lat;
        private Double lng;
        private String day;
    }
}