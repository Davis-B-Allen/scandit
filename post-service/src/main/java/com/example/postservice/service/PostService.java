package com.example.postservice.service;

import com.example.postservice.exception.PostNotFoundException;
import com.example.postservice.exception.PostServiceException;
import com.example.postservice.model.Post;
import com.example.postservice.responseobjects.PostResponse;

import java.util.List;

public interface PostService {

    PostResponse createPost(Post post, String username) throws PostServiceException, Exception;

    List<Post> deletePostsByUser(String username) throws Exception;

    List<PostResponse> getAllPosts() throws PostNotFoundException;

    List<PostResponse> getPostsByUsername(String username) throws PostNotFoundException;

    Post getPostById(Long postId);

    Long deletePost(Long postId) throws PostNotFoundException;

    Iterable<PostResponse> getPostsByPostIds(Long[] ids) throws PostNotFoundException;
}
