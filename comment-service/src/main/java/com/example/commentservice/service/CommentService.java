package com.example.commentservice.service;

import com.example.commentservice.model.Comment;
import com.example.commentservice.responseobject.CommentResponse;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public interface CommentService {

    CommentResponse createComment(Comment comment, String username, Long postId) throws JsonProcessingException;

    Comment getCommentById(Long commentId);

    Long deleteComment(Long commentId);

    List<CommentResponse> getCommentsByUsername(String username);

    List<Long> deleteCommentsByPostId(Long postId);

    List<Long> deleteCommentsByUsername(String username);

    List<CommentResponse> getCommentsByPostId(Long postId);
}
