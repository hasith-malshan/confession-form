package com.hasithmalshan.confession_form.controller;

import com.hasithmalshan.confession_form.dto.PostCreateDTO;
import com.hasithmalshan.confession_form.dto.PostDTO;
import com.hasithmalshan.confession_form.dto.PostFilterRequestDTO;
import com.hasithmalshan.confession_form.dto.PostResponseDTO;
import com.hasithmalshan.confession_form.dto.response.ApiResponse;
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
    public ResponseEntity<ApiResponse<PostDTO>> createPost(@Valid @RequestBody PostCreateDTO postCreateDTO) {
        Long userId = getCurrentUserId();
        PostDTO createdPost = postService.createPost(postCreateDTO, userId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(createdPost, "Post created successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PostDTO>> getPostById(@PathVariable Long id) {
        PostDTO postDTO = postService.getPostById(id);
        return ResponseEntity.ok(ApiResponse.success(postDTO, "Post retrieved successfully"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<PostResponseDTO>>> getAllPosts(
            @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<PostResponseDTO> posts = postService.getPostsPaginated(pageable);
        return ResponseEntity.ok(ApiResponse.success(posts, "Posts retrieved successfully"));
    }

    @GetMapping("/filter")
    public ResponseEntity<ApiResponse<Page<PostResponseDTO>>> getAllPostsFiltered(
            @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            PostFilterRequestDTO filterRequestDTO) {
        Page<PostResponseDTO> posts = postService.getPostsFilteredPaginated(pageable, filterRequestDTO);
        return ResponseEntity.ok(ApiResponse.success(posts, "Filtered posts retrieved successfully"));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<PostDTO>>> getPostsByUserId(@PathVariable Long userId) {
        List<PostDTO> posts = postService.getPostsByUserId(userId);
        return ResponseEntity.ok(ApiResponse.success(posts, "User posts retrieved successfully"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PostDTO>> updatePost(@PathVariable Long id, @Valid @RequestBody PostCreateDTO postCreateDTO) {
        PostDTO updatedPost = postService.updatePost(id, postCreateDTO);
        return ResponseEntity.ok(ApiResponse.success(updatedPost, "Post updated successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.ok(ApiResponse.noContent("Post deleted successfully"));
    }

    @GetMapping("/check/{id}")
    public ResponseEntity<ApiResponse<Boolean>> postExists(@PathVariable Long id) {
        boolean exists = postService.postExists(id);
        return ResponseEntity.ok(ApiResponse.success(exists, "Post existence checked"));
    }

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        JwtAuthDetails details = (JwtAuthDetails) authentication.getDetails();
        return details.getUserId();
    }
}
