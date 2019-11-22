package com.example.mailservice.client;

import com.example.mailservice.responseobject.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user")
public interface UserClient {

    @GetMapping("/user/{username}")
    public User getUserByUsername(@PathVariable String username);
}
