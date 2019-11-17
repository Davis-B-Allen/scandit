package com.example.commentservice.controller;

import com.example.commentservice.client.AuthClient;
import com.example.commentservice.client.PostClient;
import com.example.commentservice.model.Comment;
import com.example.commentservice.responseobject.CommentResponse;
import com.example.commentservice.responseobject.Post;
import com.example.commentservice.responseobject.User;
import com.example.commentservice.service.CommentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @DeleteMapping("/comment/{commentId}")
    public ResponseEntity deleteComment(@RequestHeader("Authorization") String bearerToken, @PathVariable Long commentId) {
        ResponseEntity<Map<String, Object>> authResponse = authClient.deleteCommentAuth(commentId, bearerToken);
        Map<String, Object> userAuthData = authResponse.getBody();
        List<String> authorities = (List<String>) userAuthData.get("authorities");
        String username = (String) userAuthData.get("username");

        Comment comment = commentService.getCommentById(commentId);
        if (username.equals(comment.getUsername()) || authorities.contains("ROLE_ADMIN")) {
            commentService.deleteComment(commentId);
            return ResponseEntity.ok("Success");
        } else {
            return new ResponseEntity("Unauthorized", HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/user/comment")
    public List<CommentResponse> getCommentsByUser(@RequestHeader("Authorization") String bearerToken) throws JsonProcessingException {
        ResponseEntity<Map<String, Object>> authResponse = authClient.getCommentsByUser(bearerToken);
        Map<String, Object> userAuthData = authResponse.getBody();
        String username = (String) userAuthData.get("username");

        List<Comment> comments = commentService.getCommentsByUsername(username);

        List<Long> postIds = comments.stream().map(Comment::getPostId).collect(Collectors.toList());
        List<Post> posts = postClient.getPostsByPostIds(postIds);

        List<CommentResponse> commentResponses = new ArrayList<>();
        for (Comment comment : comments ) {
            Long postId = comment.getPostId();
            Post post = posts.stream().filter(p -> p.getId() == postId).findFirst().orElse(null);
            User user = new User(comment.getUsername());
            CommentResponse commentResponse = new CommentResponse(comment.getId(), comment.getText(), comment.getUsername(), comment.getPostId(), user, post);
            commentResponses.add(commentResponse);
        }
        return commentResponses;
    }

    @GetMapping("/post/{postId}/comment")
    public List<CommentResponse> getCommentsByPostId(@PathVariable Long postId) {
        Post post = postClient.getPostById(postId);
        List<Comment> comments = commentService.getCommentsByPostId(postId);
        return comments.stream().map(comment -> {
           User user = new User(comment.getUsername());
           return new CommentResponse(comment.getId(), comment.getText(), comment.getUsername(), comment.getPostId(), user, post);
        }).collect(Collectors.toList());
    }

    // Service-to-service methods

    @Transactional
    @DeleteMapping("/comments/post/{postId}")
    public List<Long> deleteCommentsByPostId(@PathVariable Long postId) {
        return commentService.deleteCommentsByPostId(postId);
    }

    @Transactional
    @DeleteMapping("/comments/user/{username}")
    public List<Long> deleteCommentsByUsername(@PathVariable String username) {
        return commentService.deleteCommentsByUsername(username);
    }

}
