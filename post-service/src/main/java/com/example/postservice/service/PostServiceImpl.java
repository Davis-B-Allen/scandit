package com.example.postservice.service;

import com.example.postservice.client.CommentClient;
import com.example.postservice.model.Post;
import com.example.postservice.repository.PostRepository;
import com.example.postservice.responseobjects.PostResponse;
import com.example.postservice.responseobjects.User;
import org.springframework.beans.factory.annotation.Autowired;
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
    public PostResponse createPost(Post post, String username) {
        if (username != null && !username.equals("")) {
            post.setUsername(username);
            Post savedPost = postRepository.save(post);
            return new PostResponse(savedPost, new User(username));
        } else {
            System.out.println("USERNAME WAS EITHER NULL OR BLANK; WE REALLY SHOULDN'T BE HERE!!!");
        }
        return null;
    }

    // TODO: UNDERSTAND WHAT THIS @TRANSACTIONAL ANNOTATION IS DOING AND WHY WE NEED IT TO AVOID ERRORS
    @Transactional
    @Override
    public List<Post> deletePostsByUser(String username) {
        List<Post> deletedPosts = postRepository.deleteByUsername(username);
        List<Long> deletedPostIds = deletedPosts.stream().map(Post::getId).collect(Collectors.toList());
        System.out.println(deletedPostIds);
        // TODO: SEND REQUEST/MESSAGE TO COMMENT SERVICE WITH DELETEDPOSTIDS, TO DELETECOMMENTSBYPOSTS
        return deletedPosts;
    }

    @Override
    public List<PostResponse> getAllPosts() {
        List<PostResponse> allPostResponses = new ArrayList<>();
        Iterable<Post> allPosts = postRepository.findAll();
        for (Post post : allPosts ) {
            allPostResponses.add(new PostResponse(post, new User(post.getUsername())));
        }
        return allPostResponses;
    }

    @Override
    public List<PostResponse> getPostsByUsername(String username) {
        List<PostResponse> userPostResponses = new ArrayList<>();
        Iterable<Post> userPosts = postRepository.getPostsByUsername(username);
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
    public Long deletePost(Long postId) {
        postRepository.deleteById(postId);
        commentClient.deleteCommentsByPostId(postId);
        return postId;
    }

    // Necessary when fetching all of a user's comments (and their corresponding posts
    @Override
    public Iterable<PostResponse> getPostsByPostIds(Long[] ids) {
        List<PostResponse> allPostResponses = new ArrayList<>();
        Iterable<Post> allPosts = postRepository.getPostsByIdIn(ids);
        for (Post post : allPosts ) {
            allPostResponses.add(new PostResponse(post, new User(post.getUsername())));
        }
        return allPostResponses;
    }

}
