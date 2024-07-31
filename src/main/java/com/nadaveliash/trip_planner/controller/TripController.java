package com.nadaveliash.trip_planner.controller;

import com.nadaveliash.trip_planner.model.Trip;
import com.nadaveliash.trip_planner.model.TripRequest;
import com.nadaveliash.trip_planner.service.GeocodeService;
import com.nadaveliash.trip_planner.service.OpenAIService;
import com.nadaveliash.trip_planner.service.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("")
public class TripController {

    @Autowired
    OpenAIService openaiService;
    @Autowired
    private GeocodeService geocodeService;
    @Autowired
    private RouteService routeService;

    @RequestMapping(value = "/trip", method = RequestMethod.GET)
    public Trip getTrip(@RequestParam(value = "destination") String destination,
                        @RequestParam(value = "from", required = false) String startDate,
                        @RequestParam(value = "to", required = false) String endDate
    ) throws IOException {
        TripRequest trip = new TripRequest();
        trip.setDestination(destination);
        trip.setStartDate(startDate);
        trip.setEndDate(endDate);

        return routeService.getRoutes(openaiService.getTrip(trip));
    }

    @RequestMapping (value="/dayTrip", method = RequestMethod.GET)
    public ResponseEntity<?> getDayTrip(@RequestParam(value = "day") String day,
                                        @RequestParam(value = "places") String places,
                                        @RequestParam(value = "options", required = false) String options
    ) throws IOException {
        return new ResponseEntity<>(openaiService.getDayTrip(day, places, options), HttpStatus.OK);
    }

    @RequestMapping (value="/landmarks", method = RequestMethod.GET)
    public ResponseEntity<?> getLandmarks(@RequestParam(value = "destination") String destination,
                                        @RequestParam(value = "from", required = false) String startDate,
                                        @RequestParam(value = "to", required = false) String endDate
    ) throws IOException {
        TripRequest trip = new TripRequest();
        trip.setDestination(destination);
        trip.setStartDate(startDate);
        trip.setEndDate(endDate);
        return new ResponseEntity<>(openaiService.getTrip(trip), HttpStatus.OK);
    }

    @RequestMapping (value = "/geocode", method = RequestMethod.GET)
    public ResponseEntity<?> getLocations(@RequestParam(value = "address") String address) throws IOException {
        return new ResponseEntity<>(geocodeService.getLatLng(address), HttpStatus.OK);
    }

    @RequestMapping (value = "/route", method = RequestMethod.POST)
    public Trip getRoute(@RequestParam(value = "landmarks") String landmarks) throws IOException {
        return routeService.getRoutes(landmarks);
    }
}
