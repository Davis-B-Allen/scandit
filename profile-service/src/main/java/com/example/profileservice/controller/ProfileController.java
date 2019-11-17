package com.example.profileservice.controller;

import com.example.profileservice.client.AuthClient;
import com.example.profileservice.model.Profile;
import com.example.profileservice.responseobjects.ProfileResponse;
import com.example.profileservice.service.ProfileService;
import com.netflix.client.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.UnknownHttpStatusCodeException;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
public class ProfileController {

    @Autowired
    AuthClient authClient;

    @Autowired
    ProfileService profileService;

    @PostMapping("/profile")
    public ProfileResponse createProfile(@RequestHeader("Authorization") String bearerToken, @RequestBody Profile profile) {
        ResponseEntity<Map<String, Object>> authResponse = authClient.createProfileAuth(bearerToken);
        Map<String, Object> userAuthData = authResponse.getBody();
        String username = (String) userAuthData.get("username");

        return profileService.createProfile(profile, username);
    }

    @GetMapping("/profile")
    public ProfileResponse getProfileByUsername(@RequestHeader("Authorization") String bearerToken) {
        ResponseEntity<Map<String, Object>> authResponse = authClient.createProfileAuth(bearerToken);
        Map<String, Object> userAuthData = authResponse.getBody();
        String username = (String) userAuthData.get("username");

        return profileService.getProfileByUsername(username);
    }

    // external methods

    @DeleteMapping("/profile/{username}")
    public String deleteProfileByUsername(@PathVariable String username) {
        return profileService.deleteProfileByUsername(username);
    }
}
