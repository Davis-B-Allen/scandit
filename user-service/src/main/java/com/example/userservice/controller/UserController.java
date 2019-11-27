package com.example.userservice.controller;

import com.example.userservice.exception.LoginException;
import com.example.userservice.responseobject.JwtResponse;
import com.example.userservice.service.UserService;
import com.example.userservice.swagger.ExtraApiModels;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.userservice.model.User;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity signup(@Valid @RequestBody User user) throws LoginException {
        return ResponseEntity.ok(userService.signup(user));
    }

    @ApiOperation(value = "Login with username and password", response = JwtResponse.class)
    @PostMapping("/login")
    public ResponseEntity login(@ApiParam(value = "This should be using the UserLogin ApiModel, not User", type = "UserLogin") @RequestBody User user) throws Exception {
        return ResponseEntity.ok(userService.login(user));
    }

    @ApiIgnore
    @GetMapping("/user/{username}")
    public User getUserByUsername(@PathVariable String username) {
        return userService.getUser(username);
    }


}
