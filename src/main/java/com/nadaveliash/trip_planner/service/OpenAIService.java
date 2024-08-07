package com.nadaveliash.trip_planner.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nadaveliash.trip_planner.config.OpenAIConfig;
import com.nadaveliash.trip_planner.model.ChatResponse;
import com.nadaveliash.trip_planner.model.TripRequest;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

            String raw = res.getChoices().getFirst().getMessage().getContent();
            return cropJsonArray(raw);
        }
    }

    public String getDayTrip(String day ,String places, String options) {
        if (options != null && !options.isEmpty()) {
            options = options + ". Please add recommendations of ";
        } else {
            options = "";
        }

        String json = "{"
                + "\"model\": \"gpt-4o-mini\","
                + "\"messages\": ["
                + "{"
                + "\"role\": \"user\","
                + "\"content\": \"describe, shortly, a day trip on " + day + " (formatted dd/MM), to: " + places
                + options
                + ". Answer according to the following instructions: formatted as json object, which contains \\\"description\\\"(short description of the day trip), \\\"places\\\"(array of places names to visit in this day), \\\"recommendations\\\"(array of additional recommendations, if there is any)\""
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

            String raw = res.getChoices().getFirst().getMessage().getContent();
            return cropJsonObject(raw);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getRecommendation(String day ,String places, String options) {

        String json = "{"
                + "\"model\": \"gpt-4o-mini\","
                + "\"messages\": ["
                + "{"
                + "\"role\": \"user\","
                + "\"content\": \"Please describe 3-4 specific recommendations on " + options
                + " for a day trip on " + day + " (dd/MM format), to: " + places
                + ". Answer according to the following instructions: Without opening sentence, without any headline. short descriptions for each recommend, formatted like Json array of strings\""
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

            String raw = res.getChoices().getFirst().getMessage().getContent();
            return raw;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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

    public String cropJsonObject(String input) {
        Pattern pattern = Pattern.compile("\\{.*?\\}", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            return matcher.group();
        } else {
            return null;
        }
    }
}
