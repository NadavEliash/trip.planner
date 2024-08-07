package com.nadaveliash.trip_planner.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nadaveliash.trip_planner.config.GoogleConfig;
import com.nadaveliash.trip_planner.model.PhotoResponse;
import com.nadaveliash.trip_planner.model.PlaceResponse;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class PlaceService {

    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper om;
    private final GoogleConfig googleConfig;

        @Autowired
    public PlaceService(ObjectMapper om, GoogleConfig googleConfig) {
            this.om = om;
            this.googleConfig = googleConfig;
    }


    public List<String> getPlaceDetails(String place) throws IOException {
        String apiKey = googleConfig.getGoogleApiKey();

        String json = "{"
                + "\"textQuery\": \"" + place + "\","
                + "\"pageSize\": 1"
                + "}";

        RequestBody body = RequestBody.create(json, MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url("https://places.googleapis.com/v1/places:searchText")
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("X-Goog-Api-Key", apiKey)
                .addHeader("X-Goog-FieldMask", "places.id,places.displayName,places.photos")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            List<String> photoNames = new ArrayList<>();

            if (response.body() != null) {
                PlaceResponse.Places placeResponse = om.readValue(response.body().string(), PlaceResponse.Places.class);
                if (placeResponse != null && placeResponse.getPlaces() != null && !placeResponse.getPlaces().isEmpty()) {
                    PlaceResponse.Place firstPlace = placeResponse.getPlaces().getFirst();
                    if (firstPlace.getPhotos() != null && !firstPlace.getPhotos().isEmpty()) {
                        List<PlaceResponse.Photo> photos = firstPlace.getPhotos();
                        photos.forEach(photo -> photoNames.add(photo.getName()));
                    } else {
                        photoNames.add("No photos found.");
                    }
                } else {
                    photoNames.add("No places found.");
                }
            } else {
                photoNames.add("Response body is null");
            }
            return photoNames;
        }
    }

    public List<String> getPhotos(List<String> names) throws IOException {
            List<String> photoUrls = new ArrayList<>();
            names.forEach(name-> {
                try {
                    photoUrls.add(getPlacePhoto(name));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            return photoUrls;
    }

    public String getPlacePhoto(String name) throws IOException {
            String apiKey = googleConfig.getGoogleApiKey();

        Request request = new Request.Builder()
                .url("https://places.googleapis.com/v1/" + name + "/media?maxHeightPx=1080&maxWidthPx=1920&skipHttpRedirect=true&key=" + apiKey)
                .addHeader("Content-Type", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            if (response.body() != null) {
                PhotoResponse photo = om.readValue(response.body().string(), PhotoResponse.class);
                return photo.getPhotoUri();
            } else {
                return "Response body is null";
            }
        }
    }
}
