package com.nadaveliash.trip_planner.controller;

import com.nadaveliash.trip_planner.model.Track;
import com.nadaveliash.trip_planner.service.GeocodeService;
import com.nadaveliash.trip_planner.service.OpenAIService;
import com.nadaveliash.trip_planner.service.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("")
public class TripController {

    @Autowired
    OpenAIService openaiService;
    @Autowired
    private GeocodeService geocodeService;
    @Autowired
    private RouteService routeService;

    @RequestMapping(value = "/track", method = RequestMethod.GET)
    public Track getTrack(@RequestParam(value = "destination") String destination,
                             @RequestParam(value = "from", required = false) LocalDate startDate,
                             @RequestParam(value = "to", required = false) LocalDate endDate
    ) throws IOException {
        return routeService.getRoutes(geocodeService.getLandmarks(openaiService.getTrip(destination, startDate, endDate)));
    }

    @RequestMapping (value="/trip", method = RequestMethod.GET)
    public ResponseEntity<?> getTrip(@RequestParam(value = "destination") String destination,
                                     @RequestParam(value = "from", required = false) LocalDate startDate,
                                     @RequestParam(value = "to", required = false) LocalDate endDate
                                     ) throws IOException {
        return new ResponseEntity<>(openaiService.getTrip(destination, startDate, endDate), HttpStatus.OK);
    }

    @RequestMapping (value = "/geocode", method = RequestMethod.GET)
    public ResponseEntity<?> getLocations(@RequestParam(value = "landmarks") String strLandmarks) throws IOException {
        return new ResponseEntity<>(geocodeService.getLandmarks(strLandmarks), HttpStatus.OK);
    }

    @RequestMapping (value = "/route", method = RequestMethod.POST)
    public Track getRoute(@RequestParam(value = "landmarks") String landmarks) throws IOException {
        return routeService.getRoutes(landmarks);
    }
}
