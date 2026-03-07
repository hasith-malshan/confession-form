package com.hasithmalshan.confession_form.service;

import com.hasithmalshan.confession_form.dto.PostDTO;
import com.hasithmalshan.confession_form.dto.PostResponseDTO;
import com.hasithmalshan.confession_form.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface PostService {
    PostDTO createPost(Post post);
    PostDTO getPostById(Long id);
    Page<PostResponseDTO> getPostsPaginated(Pageable pageable);
    List<Post> getPostsByUserId(Long userId);
    Post updatePost(Long id, Post post);
    void deletePost(Long id);
    boolean postExists(Long id);
    PostDTO convertToDTO(Post post);
}

