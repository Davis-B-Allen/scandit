package com.example.commentservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "auth")
public interface AuthClient {

    @PostMapping("/comment/{postId}")
    ResponseEntity<Map<String, Object>> createCommentAuth(@PathVariable Long postId, @RequestHeader("Authorization") String bearerToken);

    @DeleteMapping("/comment/{commentId")
    ResponseEntity<Map<String, Object>> deleteCommentAuth(@PathVariable Long commentId, @RequestHeader("Authorization") String bearerToken);

    @GetMapping("/user/comment")
    ResponseEntity<Map<String, Object>> getCommentsByUser(@RequestHeader("Authorization") String bearerToken);
}
