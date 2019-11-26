package com.example.profileservice.responseobjects;

import com.example.profileservice.model.Profile;

/**
 * Creates a Profile Response object which adds a User object to the Profile.
 * The {@link User} object includes the username belonging to the profile.
 * */
public class ProfileResponse extends Profile {

    private User user;

    /**
     * The default ProfileResponse constructor.
     * */
    public ProfileResponse() {}

    /**
     * Profile constructor which takes in id, additionalemail, mobile, address, and user as arguments.
     * @param id the id of this profile
     * @param additionalemail the additional email for this profile
     * @param mobile the mobile number for this profile
     * @param address the address for this profile
     * @param user the user associated with this profile
     * */
    public ProfileResponse(Long id, String additionalemail, String mobile, String address, User user) {
        super(id, additionalemail, mobile, address, user.getUsername());
        this.user = user;
    }

    /**
     * Gets the User.
     * @return this user.
     * */
    public User getUser() {
        return user;
    }
}
