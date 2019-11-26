package com.example.profileservice.repository;

import com.example.profileservice.model.Profile;
import com.example.profileservice.responseobjects.ProfileResponse;
import org.springframework.data.repository.CrudRepository;

/**
 * Repository to access the Profile database.
 * Includes a method to search for a user's profile by their username.
 * */
public interface ProfileRepository extends CrudRepository<Profile, Long> {

    /**
     * Method to search the Profile database for a given username and return that user's profile.
     * @param username The username of the profile to search for.
     * */
    public Profile findProfileByUsername(String username);
}
