package com.example.commentservice.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import com.example.commentservice.client.PostClient;
import com.example.commentservice.controller.CommentController;
import com.example.commentservice.exception.CommentNotCreatedException;
import com.example.commentservice.exception.CommentNotFoundException;
import com.example.commentservice.exception.ExceptionHandler;
import com.example.commentservice.messenger.Sender;
import com.example.commentservice.model.Comment;
import com.example.commentservice.repository.CommentRepository;
import com.example.commentservice.responseobject.CommentResponse;
import com.example.commentservice.responseobject.Post;
import com.example.commentservice.responseobject.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {CommentServiceImpl.class, PostClient.class})
public class CommentServiceTest {

    private String username;
    private Long postId;
    private Long commentId;
    private Comment comment;
    private Post post;
    private User user;
    private CommentResponse commentResponse;

    @Autowired
    CommentService commentService;

    @MockBean
    CommentRepository commentRepository;

    @MockBean
    PostClient postClient;

    @MockBean
    Sender sender;

    public CommentServiceTest() {
        username = "user";
        postId = 1L;
        commentId = 1L;
        user = new User();
        post = new Post();
        comment = new Comment();
        MockitoAnnotations.initMocks(this);
    }

    @BeforeEach
    public void init() {
        user.setUsername(username);
        post.setUser(user);
        post.setTitle("Post Title");
        post.setDescription("This is a post description.");
        post.setId(postId);
        comment.setText("I don't like this");
        comment.setId(commentId);
        comment.setUsername(username);
        comment.setPostId(postId);
        commentResponse = new CommentResponse(comment, user, post);
    }

    @Test
    public void createComment_ValidComment_Success() throws CommentNotCreatedException, JsonProcessingException {
        when(postClient.getPostById(any())).thenReturn(post);
        when(commentRepository.save(any())).thenReturn(comment);

        CommentResponse savedCommentResponse = commentService.createComment(comment, username, postId);

        assertThat(savedCommentResponse).isEqualToComparingFieldByFieldRecursively(commentResponse);
        verify(sender, times(1)).send(anyString());
    }

    @Test
    public void createComment_DatabaseError_Failure() {
        when(postClient.getPostById(any())).thenReturn(post);
        when(commentRepository.save(any())).thenReturn(null);

        Throwable thrown = catchThrowable(() -> commentService.createComment(comment, username, postId));

        assertThat(thrown).isInstanceOf(CommentNotCreatedException.class);
        verify(sender, never()).send(anyString());
    }

    @Test
    public void getCommentById_ValidCommentId_Success() throws CommentNotFoundException {
        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(comment));
        Comment foundComment = commentService.getCommentById(commentId);
        assertThat(foundComment).isEqualToComparingFieldByFieldRecursively(comment);
    }

    @Test
    public void deleteComment_ValidId_Success() throws CommentNotFoundException {
        Long deletedCommentId = commentService.deleteComment(commentId);
        assertThat(deletedCommentId).isEqualTo(commentId);
    }

    @Test
    public void deleteComment_InvalidId_Exception() {
        doThrow(new RuntimeException()).when(commentRepository).deleteById(anyLong());
        Throwable thrown = catchThrowable(() -> commentService.deleteComment(commentId));
        assertThat(thrown).isInstanceOf(CommentNotFoundException.class);
    }

    @Test
    public void getCommentsByUsername_ValidUsername_Success() {
        List<Comment> comments = new ArrayList<>();
        List<Post> posts = new ArrayList<>();
        List<CommentResponse> commentResponses = new ArrayList<>();
        comments.add(comment);
        posts.add(post);
        commentResponses.add(commentResponse);
        when(commentRepository.findAllByUsername(username)).thenReturn(comments);
        when(postClient.getPostsByPostIds(any())).thenReturn(posts);
        List<CommentResponse> foundCommentResponses = commentService.getCommentsByUsername(username);
        for (int i = 0; i < foundCommentResponses.size(); i++) {
            assertThat(foundCommentResponses.get(i)).isEqualToComparingFieldByFieldRecursively(commentResponses.get(i));
        }
    }

    @Test
    public void deleteCommentsByPostId_ValidId_Success() throws CommentNotFoundException {
        List<Long> commentIds = new ArrayList<>();
        List<Comment> comments = new ArrayList<>();
        commentIds.add(commentId);
        comments.add(comment);
        when(commentRepository.deleteByPostId(anyLong())).thenReturn(comments);
        List<Long> deletedCommentIds = commentService.deleteCommentsByPostId(postId);
        assertThat(deletedCommentIds).isEqualTo(commentIds);
    }

    @Test
    public void deleteCommentsByPostId_InvalidId_Exception() throws CommentNotFoundException {
        when(commentRepository.deleteByPostId(anyLong())).thenReturn(null);
        Throwable thrown = catchThrowable(() -> commentService.deleteCommentsByPostId(postId));
        assertThat(thrown).isInstanceOf(CommentNotFoundException.class);
    }

    @Test
    public void deleteCommentsByUsername_ValidUsername_Success() throws CommentNotFoundException {
        List<Long> commentIds = new ArrayList<>();
        List<Comment> comments = new ArrayList<>();
        commentIds.add(commentId);
        comments.add(comment);
        when(commentRepository.deleteByUsername(anyString())).thenReturn(comments);
        List<Long> deletedCommentIds = commentService.deleteCommentsByUsername(username);
        assertThat(deletedCommentIds).isEqualTo(commentIds);
    }

    @Test
    public void deleteCommentsByUsername_InvalidUsername_Exception() throws CommentNotFoundException {
        when(commentRepository.deleteByUsername(anyString())).thenReturn(null);
        Throwable thrown = catchThrowable(() -> commentService.deleteCommentsByUsername(username));
        assertThat(thrown).isInstanceOf(CommentNotFoundException.class);
    }

    @Test
    public void getCommentsByPostId_ValidUsername_Success() {
        List<Comment> comments = new ArrayList<>();
        List<CommentResponse> commentResponses = new ArrayList<>();
        comments.add(comment);
        commentResponses.add(commentResponse);
        when(commentRepository.findAllByPostId(postId)).thenReturn(comments);
        when(postClient.getPostById(any())).thenReturn(post);
        List<CommentResponse> foundCommentResponses = commentService.getCommentsByPostId(postId);
        for (int i = 0; i < foundCommentResponses.size(); i++) {
            assertThat(foundCommentResponses.get(i)).isEqualToComparingFieldByFieldRecursively(commentResponses.get(i));
        }
    }

}
