package com.hasithmalshan.confession_form.service.impl;

import com.hasithmalshan.confession_form.dto.PostDTO;
import com.hasithmalshan.confession_form.dto.PostFilterRequestDTO;
import com.hasithmalshan.confession_form.dto.PostResponseDTO;
import com.hasithmalshan.confession_form.model.Post;
import com.hasithmalshan.confession_form.repo.PostRepository;
import com.hasithmalshan.confession_form.service.PostService;
import com.hasithmalshan.confession_form.util.PostUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private PostUtils postUtils;

    @Override
    public PostDTO createPost(Post post) {
        Post saved = postRepository.save(post);
        return convertToDTO(saved);
    }

    @Override
    public PostDTO getPostById(Long id) {
        Post byId = postRepository.findById(id).orElseThrow(() -> new RuntimeException("Post not found with id: " + id));
        return convertToDTO(byId);
    }

    @Override
    public Page<PostResponseDTO> getPostsPaginated(Pageable pageable) {
        Page<Post> latest = postRepository.findAll(pageable);
        Page<PostDTO> converted = latest.map(this::convertToDTO);
        return PostUtils.ensureAnonymity(converted);
    }

    @Override
    public Page<PostResponseDTO> getPostsFilteredPaginated(Pageable pageable, PostFilterRequestDTO filterRequestDTO) {
        Page<Post> latest = postRepository.findAll(pageable, PostUtils.buildSpec(filterRequestDTO));
        Page<PostDTO> converted = latest.map(this::convertToDTO);
        return PostUtils.ensureAnonymity(converted);
    }

    @Override
    public List<Post> getPostsByUserId(Long userId) {
        return postRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    @Override
    public Post updatePost(Long id, Post post) {
        post.setId(id);
        return postRepository.save(post);
    }

    @Override
    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }

    @Override
    public boolean postExists(Long id) {
        return postRepository.existsById(id);
    }

    @Override
    public PostDTO convertToDTO(Post post) {
        PostDTO postDTO = new PostDTO();
        postDTO.setId(post.getId());
        postDTO.setUserId(post.getUser().getId());
        postDTO.setCreatedAt(post.getCreatedAt());
        postDTO.setUpdatedAt(post.getUpdatedAt());
        postDTO.setVisibilityLevel(post.getVisibilityLevel());
        return postDTO;
    }
}

