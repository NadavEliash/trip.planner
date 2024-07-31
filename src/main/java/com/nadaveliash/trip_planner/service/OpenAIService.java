package com.nadaveliash.trip_planner.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nadaveliash.trip_planner.config.OpenAIConfig;
import com.nadaveliash.trip_planner.model.ChatResponse;
import com.nadaveliash.trip_planner.model.TripRequest;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class OpenAIService {

    private final OpenAIConfig openaiConfig;
    private final ObjectMapper om;
    private final OkHttpClient client = new OkHttpClient();

    @Autowired
    public OpenAIService(OpenAIConfig openaiConfig, ObjectMapper om) {
        this.openaiConfig = openaiConfig;
        this.om = om;
    }

    public MediaType JSON = MediaType.get("application/json; charset=utf-8");

    public String getTrip(TripRequest trip) throws IOException {

        String json = "{"
                + "\"model\": \"gpt-4o-mini\","
                + "\"messages\": ["
                + "{"
                + "\"role\": \"user\","
                + "\"content\": \"plan a trip to: " + trip.getDestination() + " from " + trip.getStartDate() + " to " + trip.getEndDate()
                + ". Answer according to the following instructions: Without opening sentence. Just a list of places to visit, without any description, formatted as an array of objects, which contains \\\"destination\\\"(exact place name), \\\"lat\\\"(place latitude), \\\"lng\\\"(place longitude) and \\\"day\\\"(visit date, formatted dd/MM)\""
                + "}"
                + "]"
                + "}";

        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(openaiConfig.getOpenAIUrl())
                .addHeader("Authorization", "Bearer " + openaiConfig.getOpenAIKey())
                .addHeader("Content-Type", "application/json")
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            assert response.body() != null;
            ChatResponse res = om.readValue(response.body().string(), ChatResponse.class);

            // System.out.println("openAI results: " + res.getChoices().getFirst().getMessage().getContent());
            return res.getChoices().getFirst().getMessage().getContent();
        }
    }

    public String getDayTrip(String day ,String places, String options) {

        String json = "{"
                + "\"model\": \"gpt-4o-mini\","
                + "\"messages\": ["
                + "{"
                + "\"role\": \"user\","
                + "\"content\": \"describe, shortly, a day trip on " + day + "(dd/MM), to: " + places
                + " please add recommendations of " + options
                + "\""
                + "}"
                + "]"
                + "}";

        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(openaiConfig.getOpenAIUrl())
                .addHeader("Authorization", "Bearer " + openaiConfig.getOpenAIKey())
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

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
