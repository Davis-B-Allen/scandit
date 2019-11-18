package com.example.authservice.controller;

import com.example.authservice.service.UserService;
import com.example.authservice.util.AuthenticationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import com.example.authservice.model.User;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    AuthenticationUtil authenticationUtil;

    // TODO: CHANGE THIS CATCHALL WILDCARD, PROBABLY (SEEMS LIKE A BAD IDEA)
//    @RequestMapping("/**")
//    public ResponseEntity<Map<String, Object>> authRoute(@RequestHeader("Authorization") String bearerToken) {
//        Authentication auth = authenticationUtil.getAuthentication();
//        String username = auth.getName();
//        List<String> authorities = auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
//        Map<String, Object> userAuthData = new HashMap<>();
//        userAuthData.put("username", username);
//        userAuthData.put("authorities", authorities);
//        return ResponseEntity.ok(userAuthData);
//    }
//    @RequestMapping("/auth")
    @RequestMapping("/**")
    public ResponseEntity<User> auth() {
        Authentication auth = authenticationUtil.getAuthentication();
        if (auth != null) {
            String username = auth.getName();
            User user = userService.getUser(username);
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.ok(null);
    }

}
