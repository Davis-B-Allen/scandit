package com.example.commentservice.controller;

import com.example.commentservice.client.PostClient;
import com.example.commentservice.model.Comment;
import com.example.commentservice.responseobject.CommentResponse;
import com.example.commentservice.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
public class CommentController {

    @Autowired
    CommentService commentService;

    @Autowired
    PostClient postClient;

    @PostMapping("/comment/{postId}")
    public CommentResponse createComment(@RequestHeader("username") String username,
                                         @RequestHeader("userRoles") String authorities,
                                         @PathVariable Long postId,
                                         @Valid @RequestBody Comment comment) throws IOException {
        return commentService.createComment(comment, username, postId);
    }

    @DeleteMapping("/comment/{commentId}")
    public ResponseEntity deleteComment(@RequestHeader("username") String username,
                                        @RequestHeader("userRoles") String authorities,
                                        @PathVariable Long commentId) {
        Comment comment = commentService.getCommentById(commentId);
        if (username.equals(comment.getUsername()) || authorities.contains("ROLE_ADMIN")) {
            commentService.deleteComment(commentId);
            return ResponseEntity.ok("Success");
        } else {
            return new ResponseEntity("Unauthorized", HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/user/comment")
    public List<CommentResponse> getCommentsByUser(@RequestHeader("username") String username) {
        return commentService.getCommentsByUsername(username);
    }

    @GetMapping("/post/{postId}/comment")
    public List<CommentResponse> getCommentsByPostId(@PathVariable Long postId) {
        return commentService.getCommentsByPostId(postId);
    }

    // Service-to-service methods
    @ApiIgnore
    @Transactional
    @DeleteMapping("/comments/post/{postId}")
    public List<Long> deleteCommentsByPostId(@PathVariable Long postId) {
        return commentService.deleteCommentsByPostId(postId);
    }

    @ApiIgnore
    @Transactional
    @DeleteMapping("/comments/user/{username}")
    public List<Long> deleteCommentsByUsername(@PathVariable String username) {
        return commentService.deleteCommentsByUsername(username);
    }

}
