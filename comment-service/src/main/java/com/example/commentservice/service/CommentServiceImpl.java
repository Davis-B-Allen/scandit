package com.example.commentservice.service;

import com.example.commentservice.model.Comment;
import com.example.commentservice.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    CommentRepository commentRepository;

    @Override
    public Comment createComment(Comment comment) {
        return commentRepository.save(comment);
    }

    @Override
    public Comment getCommentById(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(() -> {
            return new EntityNotFoundException("Could not find comment with id " + commentId);
        });
    }

    @Override
    public Long deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
        return commentId;
    }

    @Override
    public List<Comment> getCommentsByUsername(String username) {
        return commentRepository.findAllByUsername(username);
    }

    @Override
    public List<Long> deleteCommentsByPostId(Long postId) {
        List<Comment> deletedComments = commentRepository.deleteByPostId(postId);
        return deletedComments.stream().map(Comment::getId).collect(Collectors.toList());
    }

    @Override
    public List<Long> deleteCommentsByUsername(String username) {
        List<Comment> deletedComments = commentRepository.deleteByUsername(username);
        return deletedComments.stream().map(Comment::getId).collect(Collectors.toList());
    }

    @Override
    public List<Comment> getCommentsByPostId(Long postId) {
        return commentRepository.findAllByPostId(postId);
    }

}
