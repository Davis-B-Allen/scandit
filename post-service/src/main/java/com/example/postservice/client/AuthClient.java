package com.example.postservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "auth")
public interface AuthClient {

    @GetMapping("/")
    String getAuth();

}
