package com.hasithmalshan.confession_form.service;

import com.hasithmalshan.confession_form.dto.PostCreateDTO;
import com.hasithmalshan.confession_form.dto.PostDTO;
import com.hasithmalshan.confession_form.dto.PostFilterRequestDTO;
import com.hasithmalshan.confession_form.dto.PostResponseDTO;
import com.hasithmalshan.confession_form.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostService {
    PostDTO createPost(PostCreateDTO postCreateDTO, Long userId);
    PostDTO getPostById(Long id);
    Page<PostResponseDTO> getPostsPaginated(Pageable pageable);
    Page<PostResponseDTO> getPostsFilteredPaginated(Pageable pageable, PostFilterRequestDTO filterRequestDTO);
    List<PostDTO> getPostsByUserId(Long userId);
    PostDTO updatePost(Long id, PostCreateDTO postCreateDTO);
    void deletePost(Long id);
    boolean postExists(Long id);
    PostDTO convertToDTO(Post post);
}

