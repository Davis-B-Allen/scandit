package com.example.authservice.controller;

import com.example.authservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.authservice.model.User;

import javax.validation.Valid;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/")
    public String home() {
        return "valid auth";
    }


    @PostMapping("/signup")
    public ResponseEntity signup(@Valid @RequestBody User user) {
        return ResponseEntity.ok(userService.signup(user));
    }

    @RequestMapping("/login")
    public String login() {
        return "You're trying really hard to log in";
    }

}
