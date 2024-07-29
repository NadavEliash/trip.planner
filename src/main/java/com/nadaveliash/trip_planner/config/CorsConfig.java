package com.nadaveliash.trip_planner.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

import static org.springframework.http.HttpHeaders.*;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(List.of("http://localhost:3000"));

        config.setAllowedHeaders(List.of(ORIGIN, ACCESS_CONTROL_ALLOW_ORIGIN, CONTENT_TYPE, ACCEPT, AUTHORIZATION, ACCESS_CONTROL_REQUEST_METHOD, ACCESS_CONTROL_REQUEST_HEADERS, ACCESS_CONTROL_ALLOW_CREDENTIALS));
        config.setExposedHeaders(List.of(ORIGIN, ACCESS_CONTROL_ALLOW_ORIGIN, CONTENT_TYPE, ACCEPT, AUTHORIZATION, ACCESS_CONTROL_REQUEST_METHOD, ACCESS_CONTROL_REQUEST_HEADERS, ACCESS_CONTROL_ALLOW_CREDENTIALS));
        config.setAllowedMethods(List.of(GET.name(), POST.name(), PUT.name(), DELETE.name(), OPTIONS.name(), PATCH.name()));
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
}
