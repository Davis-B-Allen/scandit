package com.example.commentservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "You must provide some text in your comment")
    @NotBlank(message = "Comment text cannot be blank")
    @Column(nullable = false)
    private String text;

    @JsonIgnore
    @Column(nullable = false)
    private String username;

    @JsonIgnore
    @Column(name = "post_id", nullable = false)
    private Long postId;

    public Comment() { }

    public Comment(Long id, String text, String username, Long postId) {
        this.id = id;
        this.text = text;
        this.username = username;
        this.postId = postId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }
}
