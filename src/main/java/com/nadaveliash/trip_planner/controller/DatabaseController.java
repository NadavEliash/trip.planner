package com.nadaveliash.trip_planner.controller;

import com.nadaveliash.trip_planner.model.TripDB;
import com.nadaveliash.trip_planner.model.TripRequest;
import com.nadaveliash.trip_planner.repository.TripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/db")
public class DatabaseController {

    @Autowired
    TripRepository tripRepository;

    @PostMapping("/settrip")
    public void setTrip(@RequestBody TripDB tripDB) {
        tripRepository.save(tripDB);
    }

    @PostMapping("/gettrip")
    public TripDB getTrip(@RequestBody TripRequest tripRequest) {
        Optional<TripDB> trip = tripRepository.findByDestinationAndStartDateAndEndDate(tripRequest.getDestination(), tripRequest.getStartDate(), tripRequest.getEndDate());

        if (trip.isPresent()) {
            if (tripRequest.getStartDate().equals(trip.get().getStartDate())
            && tripRequest.getEndDate().equals(trip.get().getEndDate())) {
                return trip.get();
            }
        }
        return null;
    }
}
