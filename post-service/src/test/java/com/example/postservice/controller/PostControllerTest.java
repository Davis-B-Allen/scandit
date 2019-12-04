package com.example.postservice.controller;

import com.example.postservice.exception.ExceptionHandler;
import com.example.postservice.exception.PostNotCreatedException;
import com.example.postservice.model.Post;
import com.example.postservice.responseobjects.PostResponse;
import com.example.postservice.responseobjects.User;
import com.example.postservice.service.PostService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("qa")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {PostController.class, ExceptionHandler.class})
@WebMvcTest(PostController.class)
//@SpringBootTest
//@AutoConfigureMockMvc
public class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private User user;
    private Post post;
    private PostResponse postResponse;
    private ObjectMapper objectMapper;

    @MockBean
    PostService postService;

    public PostControllerTest() {
        user = new User("testUser");
        post = new Post();
        postResponse = new PostResponse();
    }

    //    @Before
//    public void setUp() throws Exception {
//        when(postService.createPost(post, user.getUsername()))
//                .thenReturn(postResponse);
//        when(postService.deletePost(post.getId()))
//                .thenReturn(post.getId());
//    }

    @BeforeEach
    public void init() {
        objectMapper = new ObjectMapper();
        user.setUsername("testUser");
        post.setId(1L);
        post.setTitle("first post");
        post.setDescription("post description");
        post.setUsername("testUser");
        postResponse.setId(1L);
        postResponse.setTitle("first post");
        postResponse.setDescription("post description");
        postResponse.setUsername("testUser");
    }




    // GET all posts
    @Test
    public void getPosts_Post_Success() throws Exception {
        PostResponse firstPost = new PostResponse();
        firstPost.setTitle(post.getTitle());
        firstPost.setDescription(post.getDescription());
        firstPost.setUsername(user.getUsername());

        PostResponse secondPost = new PostResponse();
        secondPost.setTitle(post.getTitle());
        secondPost.setDescription(post.getDescription());
        secondPost.setUsername(user.getUsername());

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/post/list")
                .accept(MediaType.APPLICATION_JSON);

        when(postService.getAllPosts()).thenReturn(Arrays.asList(firstPost, secondPost));

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(2)));

        verify(postService, times(1)).getAllPosts();
        verifyNoMoreInteractions(postService);
    }

    // Get posts by username
    @Test
    public void getPostsByUsername_Post_Success() throws Exception {
        PostResponse firstPost = new PostResponse(post, user);

        PostResponse secondPost = new PostResponse(post, user);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/user/post")
                .header("username", "testUser")
                .accept(MediaType.APPLICATION_JSON);

        when(postService.getPostsByUsername("testUser")).thenReturn(Arrays.asList(firstPost, secondPost));

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk());


        verify(postService, times(1)).getPostsByUsername(user.getUsername());
    }

    // Create a new post
    @Test
    public void createPost_Post_Success() throws Exception {
        User user1 = new User("testUser");

        when(postService.createPost(post, user1.getUsername())).thenReturn(postResponse);

        MvcResult result = mockMvc.perform(post("/post")
                .header("username", "testUser")
                .header("userRoles", "ROLE_USER")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(createPostInJson("first post", "heres a post description", "testUser"))
                .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andReturn();

        System.out.println(result);

        verify(postService, times(1)).createPost(any(), eq("testUser"));
        verifyNoMoreInteractions(postService);

    }

    @Test
    public void createPost_PostNotValid_Exception() throws Exception {
        post.setTitle("");

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/post")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(post));

        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest());

    }

    @Test
    public void createPost_DatabaseError_Exception() throws Exception {
        System.out.println(objectMapper.writeValueAsString(post));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/post")
                .header("username", user.getUsername())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(post));

        System.out.println("1 !!!!!");
        when(postService.createPost(any(), any())).thenThrow(new PostNotCreatedException("Error saving post to database", new Exception(), HttpStatus.CONFLICT));

        System.out.println("2 !!!!!");
        MvcResult mvcResult = mockMvc.perform(requestBuilder)
                .andExpect(status().isInternalServerError())
                .andReturn();
        System.out.println("3a !!!!!");
        System.out.println(mvcResult.getResponse().getContentAsString());
        System.out.println("3b !!!!!");
    }

    // Delete an existing post
    @Test
    public void deletePost_PostId_Success() throws Exception {
        User user1 = new User("testUser");

        String postId = "1";

        Post firstPost = new Post();
        firstPost.setId(1L);
        firstPost.setTitle("first post");
        firstPost.setDescription("first post description");
        firstPost.setUsername(user1.getUsername());

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/post/" + firstPost.getId())
                .header("username", firstPost.getUsername())
                .header("userRoles", "ROLE_ADMIN")
                .accept(MediaType.APPLICATION_JSON);

        when(postService.getPostById(anyLong())).thenReturn(firstPost);
        when(postService.deletePost(firstPost.getId())).thenReturn(firstPost.getId());

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().string(postId));

        verify(postService, times(1)).deletePost(anyLong());
    }

    @Test
    public void deletePostsByUser_PostResponse_Success() throws Exception {
        User user1 = new User("testUser");

        Post firstPost = new Post();
        firstPost.setId(1L);
        firstPost.setTitle("first post");
        firstPost.setDescription("first post description");
        firstPost.setUsername(user1.getUsername());

        Post secondPost = new Post();
        secondPost.setId(1L);
        secondPost.setTitle("first post");
        secondPost.setDescription("first post description");
        secondPost.setUsername(user1.getUsername());

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/posts/" + user.getUsername())
                .header("username", firstPost.getUsername())
                .accept(MediaType.APPLICATION_JSON);

        when(postService.deletePostsByUser(user1.getUsername())).thenReturn(Arrays.asList(firstPost, secondPost));

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk());

        verify(postService, times(1)).deletePostsByUser(user1.getUsername());
    }

    @Test
    public void getPostsByPostIds_PostResponse_Success() throws Exception {
        Long[] postIds = new Long[3];
        postIds[0] = 1L;
        postIds[1] = 2L;
        postIds[2] = 3L;

        String postIdStrings = "1;2;3";

        List<PostResponse> responses = new ArrayList<>();
        responses.add(postResponse);
        responses.add(postResponse);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/posts")
                .header("Post-Ids", postIdStrings)
                .accept(MediaType.APPLICATION_JSON);

        when(postService.getPostsByPostIds(postIds)).thenReturn(responses);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk());

        verify(postService, times(1)).getPostsByPostIds(postIds);
    }

    @Test
    public void getPostById_PostResponse_Success() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/posts/" + post.getId())
                .accept(MediaType.APPLICATION_JSON);

        when(postService.getPostById(post.getId())).thenReturn(post);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk());

        verify(postService, times(1)).getPostById(post.getId());
    }






    private static String createPostInJson(String title, String description, String username) {
        return "{ \"title\": \"" + title + "\", " +
                "\"description\":\"" + description + "\", " +
                "\"username\":\"" + username + "\"}";
    }

}
