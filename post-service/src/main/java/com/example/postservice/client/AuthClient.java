package com.example.postservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "auth")
public interface AuthClient {

    @GetMapping("/")
    String getAuth();

    // TODO: FIGURE OUT HOW TO PROPERLY GET ERROR FROM THE SERVICE WE'RE CONTACTING WITH THIS FEIGN CLIENT
    // TODO: RATHER THAN SIMPLY THROWING A GENERIC, UNHELPFUL "FEIGNEXCEPTION"
    @PostMapping("/post")
    ResponseEntity<String> createPostAuth(@RequestHeader("Authorization") String bearerToken);

}
