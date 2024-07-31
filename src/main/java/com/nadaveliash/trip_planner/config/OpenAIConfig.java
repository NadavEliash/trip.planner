package com.nadaveliash.trip_planner.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration

public class OpenAIConfig {
    @Value("${OpenAI.key}")
    private String openAIKey;

    @Value("${OpenAI.url}")
    private String openAIUrl;

    public String getOpenAIKey() {
        return  openAIKey;
    }

    public String getOpenAIUrl() {
        return openAIUrl;
    }
}
