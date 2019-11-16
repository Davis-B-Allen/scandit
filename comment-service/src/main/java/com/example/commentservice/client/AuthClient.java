package com.example.commentservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Map;

@FeignClient(name = "auth")
public interface AuthClient {

    @PostMapping("/comment/{postId}")
    ResponseEntity<Map<String, Object>> createCommentAuth(@PathVariable Long postId, @RequestHeader("Authorization") String bearerToken);

}
