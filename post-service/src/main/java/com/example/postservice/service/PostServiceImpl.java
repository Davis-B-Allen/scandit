package com.example.postservice.service;

import com.example.postservice.client.CommentClient;
import com.example.postservice.exception.PostNotCreatedException;
import com.example.postservice.exception.PostNotFoundException;
import com.example.postservice.exception.PostServiceException;
import com.example.postservice.model.Post;
import com.example.postservice.repository.PostRepository;
import com.example.postservice.responseobjects.PostResponse;
import com.example.postservice.responseobjects.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    PostRepository postRepository;

    @Autowired
    CommentClient commentClient;

    @Override
    public PostResponse createPost(Post post, String username) throws Exception {
        if (username == null || username.equals("")) {
            throw new PostServiceException("Error: you must be logged in to create a post");
        } else {
            post.setUsername(username);

            try {
                Post savedPost = postRepository.save(post);
                return new PostResponse(savedPost, new User(username));
            } catch (Exception e) {
                throw new PostNotCreatedException("Error saving post to database", e.getCause(), HttpStatus.CONFLICT);
            }

        }
    }

    // TODO: UNDERSTAND WHAT THIS @TRANSACTIONAL ANNOTATION IS DOING AND WHY WE NEED IT TO AVOID ERRORS
    @Transactional
    @Override
    public List<Post> deletePostsByUser(String username) throws Exception {
        List<Post> deletedPosts = postRepository.deleteByUsername(username);

        if (deletedPosts == null) {
            throw new PostNotFoundException("No posts found for user " + username);
        }

        List<Long> deletedPostIds = deletedPosts.stream().map(Post::getId).collect(Collectors.toList());
        System.out.println(deletedPostIds);
        // TODO: SEND REQUEST/MESSAGE TO COMMENT SERVICE WITH DELETEDPOSTIDS, TO DELETECOMMENTSBYPOSTS
        return deletedPosts;
    }

    @Override
    public List<PostResponse> getAllPosts() throws PostNotFoundException {
        List<PostResponse> allPostResponses = new ArrayList<>();
        Iterable<Post> allPosts = postRepository.findAll();

        if (allPosts == null) {
            throw new PostNotFoundException("No posts found");
        }

        for (Post post : allPosts ) {
            allPostResponses.add(new PostResponse(post, new User(post.getUsername())));
        }
        return allPostResponses;
    }

    @Override
    public List<PostResponse> getPostsByUsername(String username) throws PostNotFoundException {
        List<PostResponse> userPostResponses = new ArrayList<>();
        Iterable<Post> userPosts = postRepository.getPostsByUsername(username);

        if (userPosts == null) {
            throw new PostNotFoundException("No posts found for user " + username);
        }

        for (Post post : userPosts) {
            userPostResponses.add(new PostResponse(post, new User(post.getUsername())));
        }
        return userPostResponses;
    }

    @Override
    public Post getPostById(Long postId) {
        return postRepository.findById(postId).orElseThrow(() -> {return new EntityNotFoundException("Couldn't find a post with id " + postId);});
    }

    @Override
    public Long deletePost(Long postId) throws PostNotFoundException {
        try {
            postRepository.deleteById(postId);
            commentClient.deleteCommentsByPostId(postId);
            return postId;
        } catch (Exception e) {
            throw new PostNotFoundException("Error: no post found for post Id " + postId);
        }
    }

    // Necessary when fetching all of a user's comments (and their corresponding posts)
    @Override
    public Iterable<PostResponse> getPostsByPostIds(Long[] ids) throws PostNotFoundException {

        try {
            List<PostResponse> allPostResponses = new ArrayList<>();
            Iterable<Post> allPosts = postRepository.getPostsByIdIn(ids);
            for (Post post : allPosts ) {
                allPostResponses.add(new PostResponse(post, new User(post.getUsername())));
            }
            return allPostResponses;
        } catch (Exception e) {
            throw new PostNotFoundException("Error: no posts found for post Ids " + ids);
        }
    }

}
