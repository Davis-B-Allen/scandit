package com.example.commentservice.service;

import com.example.commentservice.model.Comment;

import java.util.List;

public interface CommentService {

    Comment createComment(Comment comment);

    Comment getCommentById(Long commentId);

    Long deleteComment(Long commentId);

    List<Comment> getCommentsByUsername(String username);

    List<Long> deleteCommentsByPostId(Long postId);

    List<Long> deleteCommentsByUsername(String username);

    List<Comment> getCommentsByPostId(Long postId);
}
