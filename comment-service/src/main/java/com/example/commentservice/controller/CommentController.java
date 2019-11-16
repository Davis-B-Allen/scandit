package com.example.commentservice.controller;

import com.example.commentservice.client.AuthClient;
import com.example.commentservice.client.PostClient;
import com.example.commentservice.model.Comment;
import com.example.commentservice.responseobject.CommentResponse;
import com.example.commentservice.responseobject.Post;
import com.example.commentservice.responseobject.User;
import com.example.commentservice.service.CommentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
public class CommentController {

    @Autowired
    CommentService commentService;

    @Autowired
    AuthClient authClient;

    @Autowired
    PostClient postClient;

    @PostMapping("/comment/{postId}")
    public CommentResponse createComment(@RequestHeader("Authorization") String bearerToken, @PathVariable Long postId, @RequestBody Comment comment) throws IOException {

        // Auth and fetch user
        ResponseEntity<Map<String, Object>> authResponse = authClient.createCommentAuth(postId, bearerToken);
        Map<String, Object> userAuthData = authResponse.getBody();
        String username = (String) userAuthData.get("username");

        // Fetch the post
        Post post = postClient.getPostById(postId);

        comment.setUsername(username);
        comment.setPostId(post.getId());

        Comment createdComment = commentService.createComment(comment);

        CommentResponse commentResponse = new CommentResponse(createdComment.getId(),
                createdComment.getText(), createdComment.getUsername(), createdComment.getPostId(), new User(username), post);
        return commentResponse;
    }

}
