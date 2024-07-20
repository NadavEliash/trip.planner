package com.nadaveliash.trip_planner.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TripController {

    @GetMapping("/")
    public String hello() {
        return "Hello World";
    }
}
