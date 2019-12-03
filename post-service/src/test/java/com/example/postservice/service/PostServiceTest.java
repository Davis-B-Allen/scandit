package com.example.postservice.service;

import com.example.postservice.model.Post;
import com.example.postservice.repository.PostRepository;
import com.example.postservice.responseobjects.PostResponse;
import com.example.postservice.responseobjects.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.eq;

public class PostServiceTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostServiceImpl postService;

    @InjectMocks
    private User user;

    @InjectMocks
    private Post post;

    @Mock
    private PostResponse postResponse;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

//    @Before
//    public void setUp() throws Exception {
////        when(postRepository.save(post))
////                .thenReturn(postResponse);
//        when(postRepository.deleteByUsername(user.getUsername()))
//                .thenReturn(Arrays.asList(post, post));
//    }

    @Before
    public void initializeDummyUser() {
        user = new User("testUser");
        user.setUsername("testUser");
    }

    @Before
    public void initializeDummyPost() {
        post = new Post();
        post.setId(1L);
        post.setTitle("first post");
        post.setDescription("post description");
        post.setUsername("testUser");
    }

    @Before
    public void initializeDummyPostResponse() {
        postResponse = new PostResponse();
        postResponse.setId(1L);
        postResponse.setTitle("first post");
        postResponse.setDescription("post description");
        postResponse.setUsername("testUser");
    }

    @Test
    public void createPost_PostResponse_Success() {
        when(postRepository.save(any())).thenReturn(postResponse);

        assertEquals(post.getTitle(), postResponse.getTitle());
    }

    @Test
    public void deletePostsByUser_ListOfPosts_Success() throws Exception {

        List<Post> deletedPosts = postRepository.deleteByUsername(user.getUsername());

        when(postRepository.deleteByUsername(user.getUsername())).thenReturn(Arrays.asList(post, post));

        verify(postRepository, times(1)).deleteByUsername(user.getUsername());
    }

    @Test
    public void getAllPosts_ListOfPostResponses_Success() {
        List<Post> posts = new ArrayList<>();
        posts.add(post);
        posts.add(post);

        when(postRepository.findAll()).thenReturn(posts);

        Iterable<Post> allPosts = postRepository.findAll();

        assertNotNull("Test returned null list, expected list of posts", allPosts);
        assertEquals(allPosts, posts);
    }

    @Test
    public void getPostsByUsername_ListOfPostResponses_Success() {
        List<Post> posts = new ArrayList<>();
        posts.add(post);
        posts.add(post);

        when(postRepository.getPostsByUsername(user.getUsername())).thenReturn(posts);

        Iterable<Post> userPosts = postRepository.getPostsByUsername(user.getUsername());

        assertNotNull("Test returned null list, expected list of posts", userPosts);
        assertEquals(userPosts, posts);
    }

    @Test
    public void getPostById_Post_Success() {
        when(postRepository.findById(post.getId())).thenReturn(java.util.Optional.ofNullable(post));

        Optional<Post> savedPost = Optional.ofNullable(post);

        Optional<Post> localPost = postRepository.findById(post.getId());

        assertNotNull("Test returned null object, expected Post", localPost);
        assertEquals(localPost, savedPost);
    }

    @Test
    public void deletePost_Long_Success() throws Exception {
//        Long deletedId = postRepository.deleteById(post.getId());
    }
}
