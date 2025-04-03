package com.model;

public class AuthResponse {
    private String token;

    // Constructor, getters, setters
    public AuthResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

