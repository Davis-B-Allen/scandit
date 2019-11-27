package com.example.postservice.service;

import com.example.postservice.exception.PostServiceException;
import com.example.postservice.model.Post;
import com.example.postservice.responseobjects.PostResponse;

import java.util.List;

public interface PostService {

    PostResponse createPost(Post post, String username) throws PostServiceException;

    List<Post> deletePostsByUser(String username);

    List<PostResponse> getAllPosts();

    List<PostResponse> getPostsByUsername(String username);

    Post getPostById(Long postId);

    Long deletePost(Long postId);

    Iterable<PostResponse> getPostsByPostIds(Long[] ids);
}
