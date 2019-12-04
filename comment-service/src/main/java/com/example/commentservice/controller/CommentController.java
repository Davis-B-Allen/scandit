package com.example.commentservice.controller;

import com.example.commentservice.exception.CommentNotCreatedException;
import com.example.commentservice.exception.CommentNotFoundException;
import com.example.commentservice.model.Comment;
import com.example.commentservice.responseobject.CommentResponse;
import com.example.commentservice.service.CommentService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
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

    @PostMapping("/comment/{postId}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "comment", value = "a comment object with just text", example = "{\n\t\"text\": \"I don't like this\"\n}", required = true, dataType = "string", paramType = "body"),
            @ApiImplicitParam(name = "Authorization", value = "JWT, Bearer Token", example = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c3IxIiwiZXhwIjoxNTcyMTE0ODY5LCJpYXQiOjE1NzIwMjg0Njl9.luy1bZ3tjzqL67BQQapA5afRamvlwb3j1-tZthYiaoYZjoLvW-5ZAQiUPG3I8m--m0F2H7EFcC5ZR1xaNed8xw", required = true, dataType = "string", paramType = "header")
    })
    public CommentResponse createComment(@ApiIgnore @RequestHeader("username") String username,
                                         @PathVariable Long postId,
                                         @Valid @RequestBody Comment comment) throws IOException, CommentNotCreatedException {
        return commentService.createComment(comment, username, postId);
    }

    @DeleteMapping("/comment/{commentId}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "JWT, Bearer Token", example = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c3IxIiwiZXhwIjoxNTcyMTE0ODY5LCJpYXQiOjE1NzIwMjg0Njl9.luy1bZ3tjzqL67BQQapA5afRamvlwb3j1-tZthYiaoYZjoLvW-5ZAQiUPG3I8m--m0F2H7EFcC5ZR1xaNed8xw", required = true, dataType = "string", paramType = "header")
    })
    public ResponseEntity deleteComment(@ApiIgnore @RequestHeader("username") String username,
                                        @ApiIgnore @RequestHeader("userRoles") String authorities,
                                        @PathVariable Long commentId) throws CommentNotFoundException {
        Comment comment = commentService.getCommentById(commentId);
        if (username.equals(comment.getUsername()) || authorities.contains("ROLE_ADMIN")) {
            commentService.deleteComment(commentId);
            return ResponseEntity.ok("Success");
        } else {
            return new ResponseEntity("Unauthorized", HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/user/comment")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "JWT, Bearer Token", example = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c3IxIiwiZXhwIjoxNTcyMTE0ODY5LCJpYXQiOjE1NzIwMjg0Njl9.luy1bZ3tjzqL67BQQapA5afRamvlwb3j1-tZthYiaoYZjoLvW-5ZAQiUPG3I8m--m0F2H7EFcC5ZR1xaNed8xw", required = true, dataType = "string", paramType = "header")
    })
    public List<CommentResponse> getCommentsByUser(@ApiIgnore @RequestHeader("username") String username) {
        return commentService.getCommentsByUsername(username);
    }

    @GetMapping("/post/{postId}/comment")
    public List<CommentResponse> getCommentsByPostId(@PathVariable Long postId) throws CommentNotFoundException {
        return commentService.getCommentsByPostId(postId);
    }

    // Service-to-service methods
    @ApiIgnore
    @Transactional
    @DeleteMapping("/comments/post/{postId}")
    public List<Long> deleteCommentsByPostId(@PathVariable Long postId) throws CommentNotFoundException {
        return commentService.deleteCommentsByPostId(postId);
    }

    @ApiIgnore
    @Transactional
    @DeleteMapping("/comments/user/{username}")
    public List<Long> deleteCommentsByUsername(@PathVariable String username) throws CommentNotFoundException {
        return commentService.deleteCommentsByUsername(username);
    }

}
