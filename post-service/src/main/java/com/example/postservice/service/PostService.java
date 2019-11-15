package com.example.postservice.service;

import com.example.postservice.model.Post;
import com.example.postservice.responseobjects.PostResponse;

import java.util.List;

public interface PostService {

    PostResponse createPost(Post post, String username);

    List<Post> deletePostsByUser(String username);

    Iterable<Post> getAllPosts();

    Iterable<Post> getPostsByUsername(String username);

    Long deletePost(Long postId);
}
