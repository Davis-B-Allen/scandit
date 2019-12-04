package com.example.profileservice.controller;

import com.example.profileservice.exception.ProfileServiceException;
import com.example.profileservice.model.Profile;
import com.example.profileservice.responseobjects.ProfileResponse;
import com.example.profileservice.service.ProfileService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiParam;
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
    @ApiImplicitParams({
            @ApiImplicitParam(name = "profile", value = "a profile object with just additional Email, mobile number and address", example = "{\n\t\"additionalemail\": \"hello@example.com\",\n\t\"mobile\": \"123-456-7890\",\n\t\"address\": \"123 4th Street, New York, NY 10011\"\n}", required = true, dataType = "string", paramType = "body"),
            @ApiImplicitParam(name = "Authorization", value = "JWT, Bearer Token", example = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c3IxIiwiZXhwIjoxNTcyMTE0ODY5LCJpYXQiOjE1NzIwMjg0Njl9.luy1bZ3tjzqL67BQQapA5afRamvlwb3j1-tZthYiaoYZjoLvW-5ZAQiUPG3I8m--m0F2H7EFcC5ZR1xaNed8xw", required = true, dataType = "string", paramType = "header")
    })
    @PostMapping("/profile")
    public ProfileResponse createProfile(@ApiIgnore @RequestHeader("username") String username, @RequestBody Profile profile) throws ProfileServiceException {
        return profileService.createProfile(profile, username);
    }

    /**
     * GET request to retrieve a user's profile.
     * The request will get the username from the current session token and use that to retrieve the corresponding user's profile.
     * */
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "JWT, Bearer Token", example = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c3IxIiwiZXhwIjoxNTcyMTE0ODY5LCJpYXQiOjE1NzIwMjg0Njl9.luy1bZ3tjzqL67BQQapA5afRamvlwb3j1-tZthYiaoYZjoLvW-5ZAQiUPG3I8m--m0F2H7EFcC5ZR1xaNed8xw", required = true, dataType = "string", paramType = "header")
    })
    @GetMapping("/profile")
    public ProfileResponse getProfileByUsername(@ApiIgnore @RequestHeader("username") String username) throws ProfileServiceException {
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
