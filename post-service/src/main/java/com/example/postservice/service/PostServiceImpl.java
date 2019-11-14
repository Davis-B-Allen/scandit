package com.example.postservice.service;

import com.example.postservice.model.Post;
import com.example.postservice.repository.PostRepository;
import com.example.postservice.responseobjects.PostResponse;
import com.example.postservice.responseobjects.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    PostRepository postRepository;

    @Override
    public PostResponse createPost(Post post, String username) {
        if (username != null && !username.equals("")) {
            post.setUsername(username);
            Post savedPost = postRepository.save(post);
            User user = new User(username);
            PostResponse postResponse = new PostResponse(post.getId(), savedPost.getTitle(), savedPost.getDescription(), user);
            return postResponse;
        } else {
            System.out.println("USERNAME WAS EITHER NULL OR BLANK; WE REALLY SHOULDN'T BE HERE!!!");
        }
        return null;
    }

    // TODO: UNDERSTAND WHAT THIS @TRANSACTIONAL ANNOTATION IS DOING AND WHY WE NEED IT TO AVOID ERRORS
    @Transactional
    @Override
    public List<Post> deletePostsByUser(String username) {
        List<Post> deletedPosts = postRepository.deleteByUsername(username);
        List<Long> deletedPostIds = deletedPosts.stream().map(Post::getId).collect(Collectors.toList());
        System.out.println(deletedPostIds);
        // TODO: SEND REQUEST/MESSAGE TO COMMENT SERVICE WITH DELETEDPOSTIDS, TO DELETECOMMENTSBYPOSTS
        return deletedPosts;
    }

}
