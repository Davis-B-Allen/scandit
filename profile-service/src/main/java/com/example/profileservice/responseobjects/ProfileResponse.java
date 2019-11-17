package com.example.profileservice.responseobjects;

import com.example.profileservice.model.Profile;

public class ProfileResponse extends Profile {

    private User user;


    public ProfileResponse() {}

    public ProfileResponse(Long id, String additionalemail, String mobile, String address, User user) {
        super(id, additionalemail, mobile, address, user.getUsername());
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
