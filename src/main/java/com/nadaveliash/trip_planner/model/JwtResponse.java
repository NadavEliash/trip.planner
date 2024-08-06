package com.nadaveliash.trip_planner.model;

import lombok.Getter;
import lombok.Setter;

public class JwtResponse {
    private String token;
    private String type = "Bearer";
    @Setter
    @Getter
    private String id;
    @Setter
    @Getter
    private String username;
    @Setter
    @Getter
    private String email;

    public JwtResponse(String accessToken, String id, String username, String email) {
        this.token = accessToken;
        this.id = id;
        this.username = username;
        this.email = email;
    }

    public String getAccessToken() {
        return token;
    }

    public void setAccessToken(String accessToken) {
        this.token = accessToken;
    }

    public String getTokenType() {
        return type;
    }

    public void setTokenType(String tokenType) {
        this.type = tokenType;
    }
}