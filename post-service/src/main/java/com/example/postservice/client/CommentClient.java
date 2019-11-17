package com.example.postservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "comment")
public interface CommentClient {

    @DeleteMapping("/comments/post/{postId}")
    List<Long> deleteCommentsByPostId(@PathVariable Long postId);

}
