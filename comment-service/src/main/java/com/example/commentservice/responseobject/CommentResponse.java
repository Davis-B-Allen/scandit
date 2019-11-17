package com.example.commentservice.responseobject;

import com.example.commentservice.model.Comment;

public class CommentResponse extends Comment {

    private User user;
    private Post post;

    public CommentResponse(Long id, String text, String username, Long postId, User user, Post post) {
        super(id, text, username, postId);
        this.user = user;
        this.post = post;
    }

    public User getUser() {
        return user;
    }

    public Post getPost() {
        return post;
    }
}
