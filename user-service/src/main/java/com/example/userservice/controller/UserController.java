package com.example.userservice.controller;

import com.example.userservice.exception.LoginException;
import com.example.userservice.exception.SignupException;
import com.example.userservice.responseobject.JwtResponse;
import com.example.userservice.service.UserService;
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

    @ApiOperation(value = "Sign up with username, email and password", response = JwtResponse.class)
    @PostMapping("/signup")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "user", value = "a user object with just username, email and password", example = "{\n\t\"username\": \"user1\",\n\t\"email\": \"user1@example.com\",\n\t\"password\": password\n}", required = true, dataType = "string", paramType = "body")
    })
    public ResponseEntity signup(@Valid @RequestBody User user) throws Exception {
        return ResponseEntity.ok(userService.signup(user));
    }

    @ApiOperation(value = "Login with username and password", response = JwtResponse.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "user", value = "a user object with just email and password", example = "{\n\t\"email\": \"user1@example.com\",\n\t\"password\": password\n}", required = true, dataType = "string", paramType = "body")
    })
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody User user) throws Exception {
        return ResponseEntity.ok(userService.login(user));
    }

    @ApiIgnore
    @GetMapping("/user/{username}")
    public User getUserByUsername(@PathVariable String username) {
        return userService.getUser(username);
    }


}
