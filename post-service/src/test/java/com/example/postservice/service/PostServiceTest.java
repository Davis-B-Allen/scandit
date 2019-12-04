package com.example.postservice.service;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.example.postservice.client.CommentClient;
import com.example.postservice.exception.PostNotCreatedException;
import com.example.postservice.exception.PostNotFoundException;
import com.example.postservice.exception.PostServiceException;
import com.example.postservice.model.Post;
import com.example.postservice.repository.PostRepository;
import com.example.postservice.responseobjects.PostResponse;
import com.example.postservice.responseobjects.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;

import javax.inject.Inject;
import java.util.*;

import static org.junit.Assert.*;
import static org.junit.Assert.assertArrayEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PostServiceTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostServiceImpl postService;

    @Mock
    private CommentClient commentClient;

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
        postResponse = new PostResponse(post, user);
    }

    @Test
    public void createPost_PostResponse_Success() throws Exception {
        when(postRepository.save(any())).thenReturn(post);

        Post savedPost = postService.createPost(post, post.getUsername());

        assertEquals(postResponse.getTitle(), savedPost.getTitle());
    }

    @Test
    public void createPost_UsernameNotFound_PostServiceException() throws Exception {
        when(postRepository.save(any())).thenReturn(null);

        Throwable thrown = catchThrowable(() -> postService.createPost(post, null));

        assertThat(thrown).isInstanceOf(PostServiceException.class);
    }

    @Test
    public void createPost_PostNotCreated_PostNotCreatedException() throws Exception {
        when(postRepository.save(any())).thenReturn(null);

        Throwable thrown = catchThrowable(() -> postService.createPost(post, user.getUsername()));

        assertThat(thrown).isInstanceOf(PostNotCreatedException.class);
    }

    @Test
    public void deletePostsByUser_ListOfPosts_Success() throws Exception {

        when(postRepository.deleteByUsername(user.getUsername())).thenReturn(Arrays.asList(post, post));

        List<Post> posts = new ArrayList<>();
        posts.add(post);
        posts.add(post);

        List<Post> deletedPosts = postService.deletePostsByUser(user.getUsername());

        assertEquals(posts, deletedPosts);

        verify(postRepository, times(1)).deleteByUsername(user.getUsername());
    }

    @Test
    public void deletePostsByUser_PostNotDeleted_PostNotFoundException() throws Exception {
        when(postRepository.deleteByUsername(user.getUsername())).thenReturn(null);

        Throwable thrown = catchThrowable(() -> postService.deletePostsByUser(user.getUsername()));

        assertThat(thrown).isInstanceOf(PostNotFoundException.class);
    }

    @Test
    public void getAllPosts_ListOfPostResponses_Success() throws PostNotFoundException {
        List<Post> posts = new ArrayList<>();
        posts.add(post);

        List<PostResponse> postResponses = new ArrayList<>();
        postResponses.add(postResponse);

        when(postService.getAllPosts()).thenReturn(postResponses);
        when(postRepository.findAll()).thenReturn(posts);

        Iterable<Post> allPosts = postRepository.findAll();
        List<PostResponse> retrievedPostResponses = postService.getAllPosts();

        assertNotNull("Test returned null list, expected list of posts", retrievedPostResponses);
        assertEquals(allPosts, posts);
        assertEquals(retrievedPostResponses.get(0).getTitle(), postResponses.get(0).getTitle());
        assertEquals(retrievedPostResponses.get(0).getDescription(), postResponses.get(0).getDescription());
        assertEquals(retrievedPostResponses.get(0).getUsername(), postResponses.get(0).getUsername());
    }

    @Test
    public void getAllPosts_PostsNotFound_PostNotFoundException() throws Exception {
        when(postRepository.findAll()).thenReturn(null);

        Throwable thrown = catchThrowable(() -> postService.getAllPosts());

        assertThat(thrown).isInstanceOf(PostNotFoundException.class);
    }

    @Test
    public void getPostsByUsername_ListOfPostResponses_Success() throws PostNotFoundException {
        List<Post> posts = new ArrayList<>();
        posts.add(post);
        posts.add(post);

        List<PostResponse> postResponses = new ArrayList<>();
        postResponses.add(new PostResponse(post, user));
        postResponses.add(new PostResponse(post, user));

        when(postRepository.getPostsByUsername(user.getUsername())).thenReturn(posts);

        List<PostResponse> userPosts = postService.getPostsByUsername(user.getUsername());

        assertNotNull("Test returned null list, expected list of posts", userPosts);
        assertEquals(userPosts.get(0).getTitle(), postResponses.get(0).getTitle());
        assertEquals(userPosts.get(0).getTitle(), postResponses.get(0).getTitle());
        assertEquals(userPosts.get(0).getDescription(), postResponses.get(0).getDescription());
        assertEquals(userPosts.get(0).getUsername(), postResponses.get(0).getUsername());
    }

    @Test
    public void getPostsByUsername_PostsNotFound_PostNotFoundException() throws Exception {
        when(postRepository.getPostsByUsername(user.getUsername())).thenReturn(null);

        Throwable thrown = catchThrowable(() -> postService.getPostsByUsername(user.getUsername()));

        assertThat(thrown).isInstanceOf(PostNotFoundException.class);
    }

    @Test
    public void getPostById_Post_Success() {
        when(postRepository.findById(post.getId())).thenReturn(java.util.Optional.ofNullable(post));

        Post servicePost = postService.getPostById(post.getId());

        assertNotNull("Test returned null object, expected Post", servicePost);
        assertEquals(post, servicePost);
    }

    @Test
    public void deletePost_Long_Success() throws Exception {

        Long deletedPostId = postService.deletePost(post.getId());

        assertEquals(post.getId(), deletedPostId);

        verify(postRepository, times(1)).deleteById(post.getId());
        verify(commentClient, times(1)).deleteCommentsByPostId(post.getId());
    }

    @Test
    public void deletePost_PostNotFound_PostNotFoundException() throws Exception {
        doThrow(new RuntimeException()).when(postRepository).deleteById(any());

        Throwable thrown = catchThrowable(() -> postService.deletePost(post.getId()));

        assertThat(thrown).isInstanceOf(PostNotFoundException.class);
    }

    @Test
    public void getPostsByPostIds_IterableOfPostResponses_Success() throws PostNotFoundException {
        List<Post> posts = new ArrayList<>();
        posts.add(post);

        List<PostResponse> postResponses = new ArrayList<>();
        postResponses.add(postResponse);

        Iterable<PostResponse> responses = postResponses;

        Long[] postIds = new Long[3];
        postIds[0] = 1L;

        when(postRepository.getPostsByIdIn(postIds)).thenReturn(posts);

        Iterable<PostResponse> userPosts = postService.getPostsByPostIds(postIds);

        assertNotNull("Test returned null list, expected list of posts", userPosts);

        Iterator resIt = responses.iterator();
        Iterator upIt = userPosts.iterator();

        while (resIt.hasNext() && upIt.hasNext()) {
            assertThat(resIt.next()).isEqualToComparingFieldByFieldRecursively(upIt.next());
        }
    }

    @Test
    public void getPostsByPostIds_PostsNotFound_PostNotFoundException() throws Exception {
        Long[] postIds = new Long[1];
        postIds[0] = 1L;

        when(postRepository.getPostsByIdIn(postIds)).thenReturn(null);

        Throwable thrown = catchThrowable(() -> postService.getPostsByPostIds(postIds));

        assertThat(thrown).isInstanceOf(PostNotFoundException.class);
    }
}
