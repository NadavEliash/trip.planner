package com.nadaveliash.trip_planner.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nadaveliash.trip_planner.config.OpenAIConfig;
import com.nadaveliash.trip_planner.model.ChatResponse;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;

@Service
public class OpenAIService {

    @Autowired
    private OpenAIConfig openaiConfig;

    @Autowired
    ObjectMapper om;

    private final OkHttpClient client = new OkHttpClient();

    public String getTrip(String destination, LocalDate startDate, LocalDate endDate) throws IOException {
        String apiKey = openaiConfig.getOpenAIKey();
        String apiUrl = openaiConfig.getOpenAIUrl();

        MediaType JSON = MediaType.get("application/json; charset=utf-8");

        String json = "{"
                + "\"model\": \"gpt-4o-mini\","
                + "\"messages\": ["
                + "{"
                + "\"role\": \"user\","
                + "\"content\": \"plan a trip to: " + destination + " from " + startDate.toString() + " to " + endDate.toString()
                + ". Answer as the following instructions: without opening sentence. Just a list of places to visit, without any description, formatted as an array of objects, which contains \\\"destination\\\"(place name) and \\\"day\\\"(visit day in the trip)\""
                + "}"
                + "]"
                + "}";

        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(apiUrl)
                .addHeader("Authorization", "Bearer " + apiKey)
                .addHeader("Content-Type", "application/json")
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            assert response.body() != null;
            ChatResponse res = om.readValue(response.body().string(), ChatResponse.class);
            return res.getChoices().getFirst().getMessage().getContent();
        }
    }
}
