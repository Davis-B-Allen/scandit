package com.example.profileservice.controller;

import com.example.profileservice.exception.ProfileServiceException;
import com.example.profileservice.model.Profile;
import com.example.profileservice.responseobjects.ProfileResponse;
import com.example.profileservice.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

/**
 * Controller for the Profile microservice to expose APIs
 * Controller provides endpoints to get a user's profile, create a new profile for a user, or delete a user's profile.
 * */
@RestController
public class UserProfileController {

    @Autowired
    ProfileService profileService;

    /**
     * POST request to create/update a user's profile.
     * This method has two functions:
     * First, it will create a new profile for a user if it doesn't already exist, and
     * second, it will update the user's current profile to the profile information the user provides.
     * */
    @PostMapping("/profile")
    public ProfileResponse createProfile(@RequestHeader("username") String username, @RequestBody Profile profile) throws ProfileServiceException {
        return profileService.createProfile(profile, username);
    }

    /**
     * GET request to retrieve a user's profile.
     * The request will get the username from the current session token and use that to retrieve the corresponding user's profile.
     * */
    @GetMapping("/profile")
    public ProfileResponse getProfileByUsername(@RequestHeader("username") String username) throws ProfileServiceException {
        return profileService.getProfileByUsername(username);
    }

    /**
     * DELETE request to delete a user's profile.
     * The request will delete the user's profile from the database for the given username.
     * */
    // external methods
    @ApiIgnore
    @DeleteMapping("/profile/{username}")
    public String deleteProfileByUsername(@PathVariable String username) throws ProfileServiceException {
        return profileService.deleteProfileByUsername(username);
    }
}
