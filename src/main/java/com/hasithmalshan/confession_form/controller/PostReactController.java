package com.hasithmalshan.confession_form.controller;

import com.hasithmalshan.confession_form.dto.PostReactRequestDTO;
import com.hasithmalshan.confession_form.dto.PostReactResponseDTO;
import com.hasithmalshan.confession_form.dto.response.ApiResponse;
import com.hasithmalshan.confession_form.service.PostReactService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/react")
@RequiredArgsConstructor
public class PostReactController {
    private final PostReactService postReactService;

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> addReactToPost(@Valid @RequestBody PostReactRequestDTO postReactRequestDTO) {
        postReactService.addReactToPost(postReactRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(null, "Reaction added successfully"));
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> removeReactFromPost(@RequestParam Long postId, @RequestParam Long userId) {
        postReactService.removeReactFromPost(postId, userId);
        return ResponseEntity.ok(ApiResponse.noContent("Reaction removed successfully"));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<ApiResponse<PostReactResponseDTO>> getReactsForPost(@PathVariable Long postId) {
        PostReactResponseDTO responseDTO = postReactService.getReactsForPost(postId);
        return ResponseEntity.ok(ApiResponse.success(responseDTO, "Reactions retrieved successfully"));
    }
}
