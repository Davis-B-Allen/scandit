package com.example.authservice.controller;

import com.example.authservice.model.User;
import com.example.authservice.service.UserService;
import com.example.authservice.util.AuthenticationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    AuthenticationUtil authenticationUtil;

    @RequestMapping("/**")
    public ResponseEntity<User> auth() {
        // Get the current authentication object
        Authentication auth = authenticationUtil.getAuthentication();

        // If there is a current authentication, fetch the user and return that user
        if (auth != null) {
            String username = auth.getName();
            User user = userService.getUser(username);
            return ResponseEntity.ok(user);
        }

        // Else, return a 200 with no body
        // Note, if the user is not authenticated, they will be turned away by spring security
        // So, this return should only be invoked for routes that require no auth
        return ResponseEntity.ok(null);
    }

}
