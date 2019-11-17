package com.example.postservice.repository;

import com.example.postservice.model.Post;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface PostRepository extends CrudRepository<Post, Long> {

    List<Post> deleteByUsername(String username);

    Iterable<Post> getPostsByUsername(String username);


    Iterable<Post> getPostsByIdIn(Long[] ids);

}
