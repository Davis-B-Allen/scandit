package com.example.postservice.controller;

import com.example.postservice.client.AuthClient;
import com.example.postservice.model.Post;
import com.example.postservice.responseobjects.PostResponse;
import com.example.postservice.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
public class PostController {

    @Autowired
    PostService postService;

    @Autowired
    AuthClient authClient;

    @PostMapping("/post")
    public PostResponse createPost(@RequestHeader("Authorization") String bearerToken, @Valid @RequestBody Post post) {
        ResponseEntity<Map<String, Object>> authResponse = authClient.createPostAuth(bearerToken);
        Map<String, Object> userAuthData = authResponse.getBody();
        String username = (String) userAuthData.get("username");
        return postService.createPost(post, username);
    }

    @DeleteMapping("/posts/{username}")
    public List<Post> deletePostsByUser(@PathVariable String username) {
        return postService.deletePostsByUser(username);
    }

}
