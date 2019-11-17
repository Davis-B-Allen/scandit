package com.example.postservice.controller;

import com.example.postservice.client.AuthClient;
import com.example.postservice.model.Post;
import com.example.postservice.responseobjects.PostResponse;
import com.example.postservice.responseobjects.User;
import com.example.postservice.service.PostService;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.lang.reflect.Array;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    @GetMapping("/post/list")
    public List<PostResponse> getAllPosts() {
        return postService.getAllPosts();
    }

    @GetMapping("/post")
    public Iterable<Post> getPostsByUsername(@RequestHeader("Authorization") String bearerToken) {
        ResponseEntity<Map<String, Object>> authResponse = authClient.createPostAuth(bearerToken);
        Map<String, Object> userAuthData = authResponse.getBody();
        String username = (String) userAuthData.get("username");
        return postService.getPostsByUsername(username);
    }

    @DeleteMapping("/post/{postId}")
    public ResponseEntity<String> deletePost(@RequestHeader("Authorization") String bearerToken, @PathVariable Long postId) {
        ResponseEntity<Map<String, Object>> authResponse = authClient.createPostAuth(bearerToken);
        Map<String, Object> userAuthData = authResponse.getBody();
        List<String> authorities = (List<String>) userAuthData.get("authorities");
        String username = (String) userAuthData.get("username");

        Post post = postService.getPostById(postId);
        if (username.equals(post.getUsername()) || authorities.contains("ROLE_ADMIN")) {
            return ResponseEntity.ok(postService.deletePost(postId).toString());
        } else {
            return new ResponseEntity<String>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }
    }






    // Service-to-service methods

    @GetMapping("/posts")
    public Iterable<PostResponse> getPostsByPostIds(@RequestHeader("Post-Ids") String ids) {
        String[] postIdStrings = ids.split(";");
        Long[] postIds = new Long[postIdStrings.length];
        for (int i = 0; i < postIdStrings.length; i++) {
            postIds[i] = new Long(Long.parseLong(postIdStrings[i]));
        }
        return postService.getPostsByPostIds(postIds);
    }

    @GetMapping("/posts/{postId}")
    public PostResponse getPostById(@PathVariable Long postId) {
        Post post = postService.getPostById(postId);
        return new PostResponse(post.getId(), post.getTitle(), post.getDescription(), new User(post.getUsername()));
    }

}
