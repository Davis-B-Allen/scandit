package com.example.profileservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Map;

@FeignClient(name = "auth")
public interface AuthClient {

    @GetMapping("/")
    String getAuth();

    @PostMapping("/profile")
    ResponseEntity<Map<String, Object>> createProfileAuth(@RequestHeader("Authorization") String bearerToken);
}
