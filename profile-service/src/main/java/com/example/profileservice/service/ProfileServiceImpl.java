package com.example.profileservice.service;

import com.example.profileservice.model.Profile;
import com.example.profileservice.repository.ProfileRepository;
import com.example.profileservice.responseobjects.ProfileResponse;
import com.example.profileservice.responseobjects.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
public class ProfileServiceImpl implements ProfileService {

    @Autowired
    ProfileRepository profileRepository;

    @Override
    public ProfileResponse createProfile(Profile profile, String username) {

        Profile userProfile = profileRepository.findProfileByUsername(username);
        if (userProfile != null) {
            profile.setId(userProfile.getId());
        }

        profile.setUsername(username);
        Profile savedProfile = profileRepository.save(profile);

        User user = new User(username);
        ProfileResponse profileResponse = new ProfileResponse(savedProfile.getId(), profile.getAdditionalEmail(), savedProfile.getMobile(), savedProfile.getAddress(), user);

        return profileResponse;
    }

    @Override
    public ProfileResponse getProfileByUsername(String username) {

        Profile userProfile = profileRepository.findProfileByUsername(username);
        if (userProfile == null) {
            throw new EntityNotFoundException("Couldn't find profile for user " + username);
        }

        User user = new User(username);

        ProfileResponse profileResponse = new ProfileResponse(userProfile.getId(), userProfile.getAdditionalEmail(), userProfile.getMobile(), userProfile.getAddress(), user);

        return profileResponse;
    }

    @Override
    public String deleteProfileByUsername(String username) {
        Profile userProfile = profileRepository.findProfileByUsername(username);

        profileRepository.delete(userProfile);

        return "Profile successfully deleted";
    }
}
