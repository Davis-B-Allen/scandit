package com.example.postservice.controller;

import com.example.postservice.exception.PostNotFoundException;
import com.example.postservice.exception.PostServiceException;
import com.example.postservice.model.Post;
import com.example.postservice.responseobjects.PostResponse;
import com.example.postservice.responseobjects.User;
import com.example.postservice.service.PostService;
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
    public PostResponse createPost(@RequestHeader("username") String username, @Valid @RequestBody Post post) throws Exception {
        return postService.createPost(post, username);
    }

    @GetMapping("/post/list")
    public List<PostResponse> getAllPosts() throws PostNotFoundException {
        return postService.getAllPosts();
    }

    @GetMapping("/user/post")
    public List<PostResponse> getPostsByUsername(@RequestHeader("username") String username) throws PostNotFoundException {
        return postService.getPostsByUsername(username);
    }

    @DeleteMapping("/post/{postId}")
    public ResponseEntity<String> deletePost(@RequestHeader("username") String username,
                                             @RequestHeader("userRoles") List<String> authorities,
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
