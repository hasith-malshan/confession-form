package com.hasithmalshan.confession_form.service;

import com.hasithmalshan.confession_form.dto.PostDTO;
import com.hasithmalshan.confession_form.model.Post;

import java.util.List;
import java.util.Optional;

public interface PostService {
    Post createPost(Post post);
    Optional<Post> getPostById(Long id);
    List<Post> getAllPosts();
    List<Post> getPostsByUserId(Long userId);
    Post updatePost(Long id, Post post);
    void deletePost(Long id);
    boolean postExists(Long id);
    PostDTO convertToDTO(Post post);
}

