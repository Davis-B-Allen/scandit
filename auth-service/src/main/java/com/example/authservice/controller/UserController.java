package com.example.authservice.controller;

import com.example.authservice.service.UserService;
import com.example.authservice.util.AuthenticationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.authservice.model.User;

import javax.validation.Valid;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    AuthenticationUtil authenticationUtil;

    @RequestMapping("/")
    public String home() {
        return "valid auth";
    }

    @PostMapping("/signup")
    public ResponseEntity signup(@Valid @RequestBody User user) {
        return ResponseEntity.ok(userService.signup(user));
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody User user) {
        return ResponseEntity.ok(userService.login(user));
    }

    // TODO: CHANGE THIS CATCHALL WILDCARD, PROBABLY (SEEMS LIKE A BAD IDEA)
    @RequestMapping("/**")
    public ResponseEntity<String> authRoute(@RequestHeader("Authorization") String bearerToken) {
        String username = authenticationUtil.getAuthentication().getName();
        return ResponseEntity.ok(username);
    }

}
