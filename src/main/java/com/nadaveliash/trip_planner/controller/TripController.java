package com.nadaveliash.trip_planner.controller;

import com.nadaveliash.trip_planner.service.GeocodeService;
import com.nadaveliash.trip_planner.service.OpenAIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;

@RestController
@RequestMapping("")
public class TripController {

    @Autowired
    OpenAIService openaiService;
    @Autowired
    private GeocodeService geocodeService;

    @RequestMapping (value="/trip", method = RequestMethod.GET)
    public ResponseEntity<?> getTrip(@RequestParam(value = "destination") String destination,
                                     @RequestParam(value = "from", required = false) LocalDate startDate,
                                     @RequestParam(value = "to", required = false) LocalDate endDate
                                     ) throws IOException {
        return new ResponseEntity<>(openaiService.getTrip(destination, startDate, endDate), HttpStatus.OK);
    }

    @RequestMapping (value = "/geocode", method = RequestMethod.GET)
    public ResponseEntity<?> getLatLng(@RequestParam(value = "address") String address) throws IOException {
        return new ResponseEntity<>(geocodeService.getLatLng(address), HttpStatus.OK);
    }
}
