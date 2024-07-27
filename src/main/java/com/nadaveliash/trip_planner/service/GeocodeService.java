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

@Service
public class GeocodeService {

    @Autowired
    GoogleConfig googleConfig;

    @Autowired
    ObjectMapper om;

    LatLng latLng = new LatLng();
    private final OkHttpClient client = new OkHttpClient();

    public LatLng getLatLng(String address) throws IOException {
        String apiKey = googleConfig.getGoogleApiKey();
        String apiUrl = googleConfig.getGeocodeUrl();

        Request request = new Request.Builder()
                .url(apiUrl + address + "&key=" + apiKey)
                .addHeader("x-rapidapi-host", "google-maps-geocoding.p.rapidapi.com")
                .addHeader("x-rapidapi-key", "")
                .get()
                .build();

        Response response = client.newCall(request).execute();
        GeocodeResponse geocodeResponse = om.readValue(response.body().string(), GeocodeResponse.class);

        if (geocodeResponse.getResults() != null && !geocodeResponse.getResults().isEmpty()) {
            GeocodeResponse.Result.Geometry.Location location = geocodeResponse.getResults().get(0).getGeometry().getLocation();
            latLng.setLat(location.getLat());
            latLng.setLng(location.getLng());
        } else {
            throw new IllegalArgumentException("No results found in the response");
        }

        return latLng;
    }

    public List<LatLng> getLocations(String JsonLandmarks) throws IOException {
        List<Landmark> landmarks = om.readValue(JsonLandmarks, new TypeReference<List<Landmark>>() {});

        List<LatLng> locations = new ArrayList<>();
        for (int i = 0; i < landmarks.size() - 1; i++) {
            LatLng location = getLatLng(landmarks.get(i).getDestination());
            locations.add(location);
        }
        return locations;
    }
}
