package com.example.commentservice.service;

import com.example.commentservice.exception.CommentNotCreatedException;
import com.example.commentservice.exception.CommentNotFoundException;
import com.example.commentservice.model.Comment;
import com.example.commentservice.responseobject.CommentResponse;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public interface CommentService {

    CommentResponse createComment(Comment comment, String username, Long postId) throws JsonProcessingException, CommentNotCreatedException;

    Comment getCommentById(Long commentId) throws CommentNotFoundException;

    Long deleteComment(Long commentId) throws CommentNotFoundException;

    List<CommentResponse> getCommentsByUsername(String username);

    List<Long> deleteCommentsByPostId(Long postId) throws CommentNotFoundException;

    List<Long> deleteCommentsByUsername(String username) throws CommentNotFoundException;

    List<CommentResponse> getCommentsByPostId(Long postId);
}
