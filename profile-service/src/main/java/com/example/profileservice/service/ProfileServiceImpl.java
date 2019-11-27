package com.example.profileservice.service;

import com.example.profileservice.exception.ProfileServiceException;
import com.example.profileservice.model.Profile;
import com.example.profileservice.repository.ProfileRepository;
import com.example.profileservice.responseobjects.ProfileResponse;
import com.example.profileservice.responseobjects.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

import com.example.profileservice.controller.UserProfileController;

/**
 * Implementation class for the {@link ProfileService}
 * Defines all its methods, as well as any other business logic needed for the Profile service
 * */
@Service
public class ProfileServiceImpl implements ProfileService {

    @Autowired
    ProfileRepository profileRepository;

    /**
     * Creates a new profile
     * @param profile The new profile information to be added to the user.
     * @param username The current user's username.
     * */
    @Override
    public ProfileResponse createProfile(Profile profile, String username) throws ProfileServiceException {

        Profile userProfile = profileRepository.findProfileByUsername(username);
        if (userProfile != null) {
            profile.setId(userProfile.getId());
        }

        profile.setUsername(username);
        Profile savedProfile = profileRepository.save(profile);

        if (savedProfile == null) {
            throw new ProfileServiceException("Database error: could not save profile");
        }

        User user = new User(username);
        ProfileResponse profileResponse = new ProfileResponse(savedProfile.getId(), profile.getAdditionalEmail(), savedProfile.getMobile(), savedProfile.getAddress(), user);

        return profileResponse;
    }

    /**
     * Gets a user's profile
     * @param username The current user's username.
     * */
    @Override
    public ProfileResponse getProfileByUsername(String username) throws ProfileServiceException {

        Profile userProfile = profileRepository.findProfileByUsername(username);

        User user = new User(username);
        if (userProfile == null) {
            throw new ProfileServiceException("Couldn't find profile for user " + username);
        }

        ProfileResponse profileResponse = new ProfileResponse(userProfile.getId(), userProfile.getAdditionalEmail(), userProfile.getMobile(), userProfile.getAddress(), user);

        return profileResponse;
    }

    /**
     * Deletes a user's profile
     * @param username The current user's username.
     * */
    @Override
    public String deleteProfileByUsername(String username) {
        Profile userProfile = profileRepository.findProfileByUsername(username);
        if (userProfile == null) {
            throw new EntityNotFoundException("Couldn't find profile for user " + username);
        }

        profileRepository.delete(userProfile);

        return "Profile successfully deleted";
    }
}
