package com.example.profileservice.service;

import com.example.profileservice.model.Profile;
import com.example.profileservice.responseobjects.ProfileResponse;

/**
 * Interface to define service layer methods.
 * See {@link ProfileServiceImpl} for method definitions.
 * */
public interface ProfileService {

    /**
     * Creates a new profile
     * @param profile the new profile information to be added to the user.
     * @param username the name of the user associated with this profile.
     * */
    ProfileResponse createProfile(Profile profile, String username);

    /**
     * Gets a user's profile
     * @param username the name of the user associated with this profile.
     * */
    ProfileResponse getProfileByUsername(String username);

    /**
     * Deletes a user's profile
     * @param username the name of the user associated with this profile.
     * */
    String deleteProfileByUsername(String username);
}
