package com.example.postservice.controller;

import com.example.postservice.exception.PostNotFoundException;
import com.example.postservice.exception.PostServiceException;
import com.example.postservice.model.Post;
import com.example.postservice.responseobjects.PostResponse;
import com.example.postservice.responseobjects.User;
import com.example.postservice.service.PostService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;

@RestController
public class PostController {

    @Autowired
    PostService postService;

    //@PreAuthorize("isAuthenticated()")
    @PostMapping("/post")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "post", value = "a post object with just title and description", example = "{\n\t\"title\": \"Would you rather...\",\n\t\"description\": \"...fight a Schwarzenegger-sized Danny Devito or a Devito-sized Arnold Schwarzenegger\"\n}", required = true, dataType = "string", paramType = "body"),
            @ApiImplicitParam(name = "Authorization", value = "JWT, Bearer Token", example = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c3IxIiwiZXhwIjoxNTcyMTE0ODY5LCJpYXQiOjE1NzIwMjg0Njl9.luy1bZ3tjzqL67BQQapA5afRamvlwb3j1-tZthYiaoYZjoLvW-5ZAQiUPG3I8m--m0F2H7EFcC5ZR1xaNed8xw", required = true, dataType = "string", paramType = "header")
    })
    public PostResponse createPost(@ApiIgnore @RequestHeader("username") String username, @Valid @RequestBody Post post) throws Exception {
        return postService.createPost(post, username);
    }

    @GetMapping("/post/list")
    public List<PostResponse> getAllPosts() throws PostNotFoundException {
        return postService.getAllPosts();
    }

    @GetMapping("/user/post")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "JWT, Bearer Token", example = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c3IxIiwiZXhwIjoxNTcyMTE0ODY5LCJpYXQiOjE1NzIwMjg0Njl9.luy1bZ3tjzqL67BQQapA5afRamvlwb3j1-tZthYiaoYZjoLvW-5ZAQiUPG3I8m--m0F2H7EFcC5ZR1xaNed8xw", required = true, dataType = "string", paramType = "header")
    })
    public List<PostResponse> getPostsByUsername(@ApiIgnore @RequestHeader("username") String username) throws PostNotFoundException {
        return postService.getPostsByUsername(username);
    }

    @DeleteMapping("/post/{postId}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "JWT, Bearer Token", example = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c3IxIiwiZXhwIjoxNTcyMTE0ODY5LCJpYXQiOjE1NzIwMjg0Njl9.luy1bZ3tjzqL67BQQapA5afRamvlwb3j1-tZthYiaoYZjoLvW-5ZAQiUPG3I8m--m0F2H7EFcC5ZR1xaNed8xw", required = true, dataType = "string", paramType = "header")
    })
    public ResponseEntity<String> deletePost(@ApiIgnore @RequestHeader("username") String username,
                                             @ApiIgnore @RequestHeader("userRoles") List<String> authorities,
                                             @PathVariable Long postId) throws PostNotFoundException {
        Post post = postService.getPostById(postId);
        if (username.equals(post.getUsername()) || authorities.contains("ROLE_ADMIN")) {
            return ResponseEntity.ok(postService.deletePost(postId).toString());
        } else {
            return new ResponseEntity<String>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }
    }

    // Service-to-service methods
    @ApiIgnore
    @DeleteMapping("/posts/{username}")
    public List<Post> deletePostsByUser(@PathVariable String username) throws Exception {
        return postService.deletePostsByUser(username);
    }

    @ApiIgnore
    @GetMapping("/posts")
    public Iterable<PostResponse> getPostsByPostIds(@RequestHeader("Post-Ids") String ids) throws PostNotFoundException {
        String[] postIdStrings = ids.split(";");
        Long[] postIds = new Long[postIdStrings.length];
        for (int i = 0; i < postIdStrings.length; i++) {
            postIds[i] = new Long(Long.parseLong(postIdStrings[i]));
        }
        return postService.getPostsByPostIds(postIds);
    }

    @ApiIgnore
    @GetMapping("/posts/{postId}")
    public PostResponse getPostById(@PathVariable Long postId) {
        Post post = postService.getPostById(postId);
        return new PostResponse(post, new User(post.getUsername()));
    }

}
