package com.example.commentservice.responseobject;

import com.example.commentservice.model.Comment;

public class CommentResponse extends Comment {

    private User user;
    private Post post;

    public CommentResponse(Comment comment, User user, Post post) {
        super(comment.getId(), comment.getText(), comment.getUsername(), comment.getPostId());
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
