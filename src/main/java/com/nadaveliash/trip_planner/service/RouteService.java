package com.nadaveliash.trip_planner.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nadaveliash.trip_planner.config.GoogleConfig;
import com.nadaveliash.trip_planner.model.Landmark;
import com.nadaveliash.trip_planner.model.LatLng;
import com.nadaveliash.trip_planner.model.Trip;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public Trip getRoutes(String strLandmarks) throws IOException {
        String jsonLandmarks = cropJsonArray(strLandmarks);
        List<Landmark> landmarks = om.readValue(jsonLandmarks, new TypeReference<List<Landmark>>() {});

        if (landmarks == null || landmarks.size() < 2) {
            throw new IllegalArgumentException("At least two locations (origin and destination) are required");
        }

        List<String> routes = new ArrayList<>();
        for (int i = 0; i < landmarks.size() - 1; i++) {
            LatLng origin = new LatLng();
            origin.setLat(landmarks.get(i).getLat()+"");
            origin.setLng(landmarks.get(i).getLng()+"");

            LatLng destination = new LatLng();
            destination.setLat(landmarks.get(i + 1).getLat()+"");
            destination.setLng(landmarks.get(i + 1).getLng()+"");

            String route = getRoute(origin, destination);
            routes.add(route);
        }

        Trip trip = new Trip();
        trip.setRoutes(routes);
        trip.setLandmarks(landmarks);

        return trip;
    }

    public String cropJsonArray(String input) {
        Pattern pattern = Pattern.compile("\\[\\s*\\{.*?\\}\\s*\\]", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            return matcher.group();
        } else {
            return null;
        }
    }
}
