package com.nadaveliash.trip_planner.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nadaveliash.trip_planner.config.GoogleConfig;
import com.nadaveliash.trip_planner.model.LatLng;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class RouteService {
    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper om;
    private final GoogleConfig googleConfig;

    @Autowired
    public RouteService(ObjectMapper om, GoogleConfig googleConfig) {
        this.om = om;
        this.googleConfig = googleConfig;
    }

    public String getRoute(LatLng origin, LatLng destination) throws IOException {
        String apiKey = googleConfig.getGoogleApiKey();

        String json = "{"
                + "\"origin\":{"
                + "\"location\":{"
                + "\"latLng\":{"
                + "\"latitude\": " + origin.getLat() + ","
                + "\"longitude\": " + origin.getLng()
                + "}"
                + "}"
                + "},"
                + "\"destination\":{"
                + "\"location\":{"
                + "\"latLng\":{"
                + "\"latitude\": " + destination.getLat() + ","
                + "\"longitude\": " + destination.getLng()
                + "}"
                + "}"
                + "},"
                + "\"travelMode\": \"DRIVE\","
                + "\"routingPreference\": \"TRAFFIC_UNAWARE\","
                + "\"computeAlternativeRoutes\": false,"
                + "\"routeModifiers\": {"
                + "\"avoidTolls\": false,"
                + "\"avoidHighways\": false,"
                + "\"avoidFerries\": false"
                + "},"
                + "\"languageCode\": \"en-US\","
                + "\"units\": \"IMPERIAL\""
                + "}";

        RequestBody body = RequestBody.create(json, MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url("https://routes.googleapis.com/directions/v2:computeRoutes")
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("X-Goog-Api-Key", apiKey)
                .addHeader("X-Goog-FieldMask", "routes.duration,routes.distanceMeters,routes.polyline.encodedPolyline")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            assert response.body() != null;
            return response.body().string();
        }
    }

    public List<String> getRoutes(String JsonLocations) throws IOException {
        List<LatLng> locations = om.readValue(JsonLocations, new TypeReference<List<LatLng>>() {});

        if (locations == null || locations.size() < 2) {
            throw new IllegalArgumentException("At least two locations (origin and destination) are required");
        }

        List<String> routes = new ArrayList<>();
        for (int i = 0; i < locations.size() - 1; i++) {
            LatLng origin = locations.get(i);
            LatLng destination = locations.get(i + 1);
            String route = getRoute(origin, destination);
            routes.add(route);
        }
        return routes;
    }
}
