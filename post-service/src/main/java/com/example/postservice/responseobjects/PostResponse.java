package com.example.postservice.responseobjects;

import com.example.postservice.model.Post;

public class PostResponse extends Post {

    private User user;

    public PostResponse(Post post, User user) {
        super(post.getId(), post.getTitle(), post.getDescription(), user.getUsername());
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
