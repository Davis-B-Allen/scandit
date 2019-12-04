package com.example.commentservice.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.commentservice.exception.CommentNotCreatedException;
import com.example.commentservice.exception.CommentNotFoundException;
import com.example.commentservice.exception.ExceptionHandler;
import com.example.commentservice.model.Comment;
import com.example.commentservice.responseobject.CommentResponse;
import com.example.commentservice.responseobject.Post;
import com.example.commentservice.responseobject.User;
import com.example.commentservice.service.CommentServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {CommentController.class, ExceptionHandler.class})
@WebMvcTest(CommentController.class)
public class CommentControllerTest {

    private Long postId;
    private Comment comment;
    private String username;
    private User user;
    private Post post;
    private CommentResponse commentResponse;
    private ObjectMapper objectMapper;

    public CommentControllerTest() {
        postId = 1L;
        comment = new Comment();
        username = "user";
        user = new User(username);
        post = new Post(postId, "Post Title", "A post descritpion.", user);
        objectMapper = new ObjectMapper();
    }

    @BeforeEach
    public void init() {
        comment.setId(1L);
        comment.setText("I dont like this");
        comment.setPostId(postId);
        comment.setUsername(username);
        commentResponse = new CommentResponse(comment, user, post);
    }

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CommentServiceImpl commentService;

    @Test
    public void createComment_ValidComment_Success() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/comment/" + postId)
                .header("username", username)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(comment));

        when(commentService.createComment(any(), any(), any())).thenReturn(commentResponse);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(commentResponse)));
    }

    @Test
    public void createComment_InvalidComment_Exception() throws Exception {
        comment.setText("");
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/comment/" + postId)
                .header("username", username)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(comment));

        when(commentService.createComment(any(), any(), any())).thenReturn(commentResponse);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createComment_DatabaseError_Exception() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/comment/" + postId)
                .header("username", username)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(comment));

        when(commentService.createComment(any(), any(), any())).thenThrow(new CommentNotCreatedException("Error: unable to create comment"));

        mockMvc.perform(requestBuilder)
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void deleteComment_Authorized_Success() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/comment/" + comment.getId())
                .header("username", username)
                .header("userRoles", "ROLE_USER");

        when(commentService.getCommentById(any())).thenReturn(comment);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk());
    }

    @Test
    public void deleteComment_Unauthorized_Failure() throws Exception {
        comment.setUsername("notUser");
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/comment/" + comment.getId())
                .header("username", username)
                .header("userRoles", "ROLE_USER");

        when(commentService.getCommentById(any())).thenReturn(comment);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void getCommentsByUser_ValidUsername_Success() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/user/comment")
                .header("username", username)
                .accept(MediaType.APPLICATION_JSON);

        List<CommentResponse> commentResponses = new ArrayList<>();
        commentResponses.add(commentResponse);
        when(commentService.getCommentsByUsername(any())).thenReturn(commentResponses);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(commentResponses)));
    }

    @Test
    public void getCommentsByPostId_ValidPostId_Success() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/post/" + postId + "/comment")
                .accept(MediaType.APPLICATION_JSON);

        List<CommentResponse> commentResponses = new ArrayList<>();
        commentResponses.add(commentResponse);
        when(commentService.getCommentsByPostId(any())).thenReturn(commentResponses);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(commentResponses)));
    }

    @Test
    public void deleteCommentsByPostId_ValidPostId_Success() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/comments/post/" + postId);

        List<Long> deleteCommentIds = new ArrayList<>();
        deleteCommentIds.add(comment.getId());
        when(commentService.deleteCommentsByPostId(any())).thenReturn(deleteCommentIds);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(deleteCommentIds)));
    }

    @Test
    public void deleteCommentsByPostId_NoComments_Exception() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/comments/post/42");

        when(commentService.deleteCommentsByPostId(any())).thenThrow(new CommentNotFoundException("Error: no comments found for post id " + postId));

        mockMvc.perform(requestBuilder)
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteCommentsByUsername_ValidUsername_Success() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/comments/user/" + username);

        List<Long> deleteCommentIds = new ArrayList<>();
        deleteCommentIds.add(comment.getId());
        when(commentService.deleteCommentsByUsername(any())).thenReturn(deleteCommentIds);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(deleteCommentIds)));
    }

}
