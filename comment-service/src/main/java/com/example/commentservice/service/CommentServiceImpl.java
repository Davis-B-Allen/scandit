package com.example.commentservice.service;

import com.example.commentservice.client.PostClient;
import com.example.commentservice.model.Comment;
import com.example.commentservice.repository.CommentRepository;
import com.example.commentservice.responseobject.CommentResponse;
import com.example.commentservice.responseobject.Post;
import com.example.commentservice.responseobject.User;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Override
    public CommentResponse createComment(Comment comment, String username, Long postId) {
        Post post = postClient.getPostById(postId);
        comment.setUsername(username);
        comment.setPostId(post.getId());
        Comment createdComment = commentRepository.save(comment);
        return new CommentResponse(createdComment, new User(username), post);
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
    public List<CommentResponse> getCommentsByPostId(Long postId) {
        Post post = postClient.getPostById(postId);
        List<Comment> comments = commentRepository.findAllByPostId(postId);
        return comments.stream().map(comment -> {
            return new CommentResponse(comment, new User(comment.getUsername()), post);
        }).collect(Collectors.toList());
    }

}
