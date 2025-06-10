package com.annandam_inn.dto;

public class TokenResponse {

    private String type = "Bearer";
    private String token;

    //Generate Getters and Setters
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
