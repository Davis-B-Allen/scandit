package com.example.commentservice.service;

import com.example.commentservice.client.PostClient;
import com.example.commentservice.exception.CommentNotCreatedException;
import com.example.commentservice.exception.CommentNotFoundException;
import com.example.commentservice.messenger.Sender;
import com.example.commentservice.model.Comment;
import com.example.commentservice.repository.CommentRepository;
import com.example.commentservice.responseobject.CommentResponse;
import com.example.commentservice.responseobject.Post;
import com.example.commentservice.responseobject.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    PostClient postClient;

    @Autowired
    Sender sender;

    @Override
    public CommentResponse createComment(Comment comment, String username, Long postId) throws JsonProcessingException, CommentNotCreatedException {
        ObjectMapper objectMapper = new ObjectMapper();
        Post post = postClient.getPostById(postId);
        comment.setUsername(username);
        comment.setPostId(post.getId());
        Comment createdComment = commentRepository.save(comment);

        if (createdComment == null) {
            throw new CommentNotCreatedException("Error: unable to create comment");
        } else {
            CommentResponse commentResponse = new CommentResponse(createdComment, new User(username), post);
            sender.send(objectMapper.writeValueAsString(commentResponse));
            return commentResponse;
        }

    }

    @Override
    public Comment getCommentById(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(() -> new EntityNotFoundException("Could not find comment with id " + commentId));
    }

    @Override
    public Long deleteComment(Long commentId) throws CommentNotFoundException {
        try {
            commentRepository.deleteById(commentId);
        } catch (Exception e) {
            throw new CommentNotFoundException("Error: no comment found with id " + commentId, HttpStatus.NOT_FOUND);
        }
        return commentId;
    }

    @Override
    public List<CommentResponse> getCommentsByUsername(String username) {
        List<Comment> comments = commentRepository.findAllByUsername(username);

        List<Long> postIds = comments.stream().map(Comment::getPostId).distinct().collect(Collectors.toList());
        List<Post> posts = postClient.getPostsByPostIds(postIds);
        List<CommentResponse> commentResponses = new ArrayList<>();
        for (Comment comment : comments ) {
            Long postId = comment.getPostId();
            Post post = posts.stream().filter(p -> p.getId() == postId).findFirst().orElse(null);
            CommentResponse commentResponse = new CommentResponse(comment, new User(comment.getUsername()), post);
            commentResponses.add(commentResponse);
        }
        return commentResponses;
    }

    @Override
    public List<Long> deleteCommentsByPostId(Long postId) throws CommentNotFoundException {
        List<Comment> deletedComments = commentRepository.deleteByPostId(postId);

        if (deletedComments == null) {
            throw new CommentNotFoundException("Error: no comments found for post id " + postId);
        }

        return deletedComments.stream().map(Comment::getId).collect(Collectors.toList());
    }

    @Override
    public List<Long> deleteCommentsByUsername(String username) throws CommentNotFoundException {
        List<Comment> deletedComments = commentRepository.deleteByUsername(username);

        if (deletedComments == null) {
            throw new CommentNotFoundException("Error: no comments found for user " + username);
        }

        return deletedComments.stream().map(Comment::getId).collect(Collectors.toList());
    }

    @Override
    public List<CommentResponse> getCommentsByPostId(Long postId) throws CommentNotFoundException {
        Post post = postClient.getPostById(postId);
        List<Comment> comments = commentRepository.findAllByPostId(postId);

        if (comments == null) {
            throw new CommentNotFoundException("No comments found for post id " + postId);
        }

        return comments.stream().map(comment -> {
            return new CommentResponse(comment, new User(comment.getUsername()), post);
        }).collect(Collectors.toList());
    }

}
