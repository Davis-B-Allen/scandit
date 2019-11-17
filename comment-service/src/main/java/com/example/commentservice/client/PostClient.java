package com.example.commentservice.client;

import com.example.commentservice.responseobject.Post;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Body;
import feign.HeaderMap;
import feign.Headers;
import feign.Param;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;
import java.util.Map;

@FeignClient(name = "post")
public interface PostClient {

//    @GetMapping(value = "/posts/{postId}", consumes = "application/json")
//    Post getPostById(@PathVariable Long postId);

    @GetMapping(value = "/posts/{postId}", consumes = "application/json")
    Post getPostById(@PathVariable Long postId);

    @GetMapping("/posts")
    List<Post> getPostsByPostIds(@RequestHeader("Post-Ids") List<Long> ids);

}
