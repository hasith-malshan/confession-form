package com.hasithmalshan.confession_form.service.impl;

import com.hasithmalshan.confession_form.dto.PostCreateDTO;
import com.hasithmalshan.confession_form.dto.PostDTO;
import com.hasithmalshan.confession_form.dto.PostFilterRequestDTO;
import com.hasithmalshan.confession_form.dto.PostResponseDTO;
import com.hasithmalshan.confession_form.exception.ResourceNotFoundException;
import com.hasithmalshan.confession_form.model.Post;
import com.hasithmalshan.confession_form.model.User;
import com.hasithmalshan.confession_form.repo.PostRepository;
import com.hasithmalshan.confession_form.repo.UserRepository;
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
    private final UserRepository userRepository;

    @Override
    public PostDTO createPost(PostCreateDTO postCreateDTO, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        Post post = new Post();
        post.setUser(user);
        post.setContent(postCreateDTO.getContent());
        post.setMood(postCreateDTO.getMood());
        post.setCategory(postCreateDTO.getCategory());
        post.setVisibilityLevel(postCreateDTO.getVisibilityLevel());

        Post saved = postRepository.save(post);
        return convertToDTO(saved);
    }

    @Override
    public PostDTO getPostById(Long id) {
        Post byId = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
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
        Page<Post> latest = postRepository.findAll(PostUtils.buildSpec(filterRequestDTO), pageable);
        Page<PostDTO> converted = latest.map(this::convertToDTO);
        return PostUtils.ensureAnonymity(converted);
    }

    @Override
    public List<PostDTO> getPostsByUserId(Long userId) {
        return postRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    public PostDTO updatePost(Long id, PostCreateDTO postCreateDTO) {
        Post existingPost = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));

        existingPost.setContent(postCreateDTO.getContent());
        existingPost.setMood(postCreateDTO.getMood());
        existingPost.setCategory(postCreateDTO.getCategory());
        existingPost.setVisibilityLevel(postCreateDTO.getVisibilityLevel());

        Post updatedPost = postRepository.save(existingPost);
        return convertToDTO(updatedPost);
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
        postDTO.setContent(post.getContent());
        postDTO.setMood(post.getMood().name());
        postDTO.setCategory(post.getCategory().name());
        postDTO.setCreatedAt(post.getCreatedAt());
        postDTO.setUpdatedAt(post.getUpdatedAt());
        postDTO.setVisibilityLevel(post.getVisibilityLevel());
        return postDTO;
    }
}

