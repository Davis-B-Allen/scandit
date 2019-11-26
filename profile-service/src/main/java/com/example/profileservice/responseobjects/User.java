package com.example.profileservice.responseobjects;

public class User {

    private String username;

    /**
     * The default User constructor.
     * */
    public User() {}

    /**
     * Constructor which takes username as an argument.
     * @param username the name of the user associated with this profile.
     * */
    public User(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
