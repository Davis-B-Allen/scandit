package com.example.postservice.responseobjects;

import com.example.postservice.model.Post;

public class PostResponse extends Post {

    private User user;

    public PostResponse(Long id, String title, String description, User user) {
        super(id, title, description, user.getUsername());
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
