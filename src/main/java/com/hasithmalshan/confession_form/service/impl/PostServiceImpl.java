package com.hasithmalshan.confession_form.service.impl;

import com.hasithmalshan.confession_form.dto.PostDTO;
import com.hasithmalshan.confession_form.model.Post;
import com.hasithmalshan.confession_form.repo.PostRepository;
import com.hasithmalshan.confession_form.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    @Override
    public PostDTO createPost(Post post) {
        Post saved = postRepository.save(post);
        return convertToDTO(saved);
    }

    @Override
    public PostDTO getPostById(Long id) {
        Post byId = postRepository.findById(id).orElseThrow(()-> new RuntimeException("Post not found with id: " + id));
        return convertToDTO(byId);
    }

    @Override
    public List<Post> getAllPosts() {
        return postRepository.findAll();
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

