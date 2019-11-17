package com.example.profileservice.service;

import com.example.profileservice.model.Profile;
import com.example.profileservice.responseobjects.ProfileResponse;

public interface ProfileService {

    ProfileResponse createProfile(Profile profile, String username);

    ProfileResponse getProfileByUsername(String username);

    String deleteProfileByUsername(String username);
}
