package com.example.profileservice.controller;

import com.example.profileservice.model.Profile;
import com.example.profileservice.responseobjects.ProfileResponse;
import com.example.profileservice.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@RestController
public class UserProfileController {

    @Autowired
    ProfileService profileService;

    @PostMapping("/profile")
    public ProfileResponse createProfile(@RequestHeader("username") String username, @RequestBody Profile profile) {
        return profileService.createProfile(profile, username);
    }

    @GetMapping("/profile")
    public ProfileResponse getProfileByUsername(@RequestHeader("username") String username) {
        System.out.println(username);
        return profileService.getProfileByUsername(username);
    }

    // external methods
    @ApiIgnore
    @DeleteMapping("/profile/{username}")
    public String deleteProfileByUsername(@PathVariable String username) {
        return profileService.deleteProfileByUsername(username);
    }
}
