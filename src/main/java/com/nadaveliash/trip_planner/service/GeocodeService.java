package com.nadaveliash.trip_planner.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nadaveliash.trip_planner.config.GoogleConfig;
import com.nadaveliash.trip_planner.model.GeocodeResponse;
import com.nadaveliash.trip_planner.model.LatLng;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

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
            latLng.lat = location.getLat();
            latLng.lng = location.getLng();
        } else {
            throw new IllegalArgumentException("No results found in the response");
        }



        return latLng;
    }
}
