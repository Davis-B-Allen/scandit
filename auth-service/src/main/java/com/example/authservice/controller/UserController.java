package com.example.authservice.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @RequestMapping("/")
    public String home() {
        return "valid auth";
    }

    @RequestMapping("/signup")
    public String signup() {
        return "You're trying really hard to sign up";
    }

    @RequestMapping("/login")
    public String login() {
        return "You're trying really hard to log in";
    }

}
