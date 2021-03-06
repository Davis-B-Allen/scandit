package com.example.userservice.responseobject;

public class JwtResponse {

    private String token;
    private String username;

    public JwtResponse() { }

    public JwtResponse(String token, String username) {
        this.token = token;
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public String getUsername() {
        return username;
    }
}
