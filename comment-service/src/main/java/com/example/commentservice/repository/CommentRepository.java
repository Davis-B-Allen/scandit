package com.example.commentservice.repository;

import com.example.commentservice.model.Comment;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CommentRepository extends CrudRepository<Comment, Long> {

    List<Comment> findAllByUsername(String username);

    List<Comment> deleteByPostId(Long postId);

    List<Comment> deleteByUsername(String username);

    List<Comment> findAllByPostId(Long postId);
}
