package com.nadaveliash.trip_planner.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GoogleConfig {
    @Value("${GoogleApi.key}")
    private String GoogleApiKey;

    @Value("${Geocode.url}")
    private String GeocodeUrl;

    public String getGoogleApiKey() {
        return GoogleApiKey;
    }

    public String getGeocodeUrl() {
        return GeocodeUrl;
    }
}
