package com.hasithmalshan.confession_form.controller;

import com.hasithmalshan.confession_form.dto.PostCreateDTO;
import com.hasithmalshan.confession_form.dto.PostDTO;
import com.hasithmalshan.confession_form.dto.PostFilterRequestDTO;
import com.hasithmalshan.confession_form.dto.PostResponseDTO;
import com.hasithmalshan.confession_form.model.Post;
import com.hasithmalshan.confession_form.security.JwtAuthDetails;
import com.hasithmalshan.confession_form.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostDTO> createPost(@Valid @RequestBody PostCreateDTO postCreateDTO) {
        Long userId = getCurrentUserId();
        PostDTO createdPost = postService.createPost(postCreateDTO, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPost);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDTO> getPostById(@PathVariable Long id) {
        PostDTO postDTO = postService.getPostById(id);
        if (postDTO != null) {
            return ResponseEntity.ok(postDTO);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<Page<PostResponseDTO>> getAllPosts(
            @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<PostResponseDTO> posts = postService.getPostsPaginated(pageable);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/filter")
    public ResponseEntity<Page<PostResponseDTO>> getAllPostsFiltered(
            @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            PostFilterRequestDTO filterRequestDTO) {
        Page<PostResponseDTO> posts = postService.getPostsFilteredPaginated(pageable, filterRequestDTO);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Post>> getPostsByUserId(@PathVariable Long userId) {
        List<Post> posts = postService.getPostsByUserId(userId);
        if (!posts.isEmpty()) {
            return ResponseEntity.ok(posts);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Post> updatePost(@PathVariable Long id, @Valid @RequestBody PostCreateDTO postCreateDTO) {
        Post updatedPost = postService.updatePost(id, postCreateDTO);
        if (updatedPost != null) {
            return ResponseEntity.ok(updatedPost);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        if (postService.postExists(id)) {
            postService.deletePost(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/check/{id}")
    public ResponseEntity<Boolean> postExists(@PathVariable Long id) {
        boolean exists = postService.postExists(id);
        return ResponseEntity.ok(exists);
    }

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        JwtAuthDetails details = (JwtAuthDetails) authentication.getDetails();
        return details.getUserId();
    }
}
