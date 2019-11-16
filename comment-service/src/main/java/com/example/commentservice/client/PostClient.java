package com.example.commentservice.client;

import com.example.commentservice.responseobject.Post;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "post")
public interface PostClient {

//    @GetMapping(value = "/posts/{postId}", consumes = "application/json")
//    Post getPostById(@PathVariable Long postId);

    @GetMapping(value = "/posts/{postId}", consumes = "application/json")
    Post getPostById(@PathVariable Long postId);

}
