package com.nadaveliash.trip_planner.repository;

import com.nadaveliash.trip_planner.model.TripDB;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface TripRepository extends MongoRepository<TripDB, String> {
    Optional<TripDB> findByDestinationAndStartDateAndEndDate(String destination, String startDate, String endDate);
}