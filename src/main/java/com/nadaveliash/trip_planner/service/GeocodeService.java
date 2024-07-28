package com.nadaveliash.trip_planner.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nadaveliash.trip_planner.config.GoogleConfig;
import com.nadaveliash.trip_planner.model.GeocodeResponse;
import com.nadaveliash.trip_planner.model.Landmark;
import com.nadaveliash.trip_planner.model.LatLng;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class GeocodeService {

    @Autowired
    GoogleConfig googleConfig;

    @Autowired
    ObjectMapper om;


    private final OkHttpClient client = new OkHttpClient();

    public Landmark getLatLng(String address, String day) throws IOException {
        String apiKey = googleConfig.getGoogleApiKey();
        String apiUrl = googleConfig.getGeocodeUrl();

        Landmark landmark = new Landmark();
        landmark.setDestination(address);
        landmark.setDay(day);
        Request request = new Request.Builder()
                .url(apiUrl + address + "&key=" + apiKey)
                .addHeader("x-rapidapi-host", "google-maps-geocoding.p.rapidapi.com")
                .addHeader("x-rapidapi-key", "")
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            assert response.body() != null;
            String responseBody = response.body().string();
            GeocodeResponse geocodeResponse = om.readValue(responseBody, GeocodeResponse.class);

            if (geocodeResponse.getResults() != null && !geocodeResponse.getResults().isEmpty()) {
                GeocodeResponse.Result.Geometry.Location location = geocodeResponse.getResults().get(0).getGeometry().getLocation();
                landmark.setLat(location.getLat());
                landmark.setLng(location.getLng());
            } else {
                throw new IllegalArgumentException("No results found in the response");
            }
        } catch (Exception e) {
            System.err.println("Error in getLatLng: " + e.getMessage());
        }

        return landmark;
    }

    public String getLandmarks(String strLandmarks) throws IOException {
        List<Landmark> landmarks = new ArrayList<>();

        String jsonLandmarks = cropJsonArray(strLandmarks);
        if (jsonLandmarks != null) {
            List<Landmark> addresses = om.readValue(jsonLandmarks, new TypeReference<List<Landmark>>() {
            });

            for (int i = 0; i < addresses.size() - 1; i++) {
                Landmark landmark = getLatLng(addresses.get(i).getDestination(), addresses.get(i).getDay());
                landmarks.add(landmark);
            }
        }

        // System.out.println("landmarks: " + landmarks);
        return landmarks.toString();
    }

    public static String cropJsonArray(String input) {
        Pattern pattern = Pattern.compile("\\[\\s*\\{.*?\\}\\s*\\]", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            return matcher.group();
        } else {
            return null;
        }
    }
}