package com.hasithmalshan.confession_form.controller;

import com.hasithmalshan.confession_form.dto.PostReactDTO;
import com.hasithmalshan.confession_form.dto.PostReactRequestDTO;
import com.hasithmalshan.confession_form.dto.PostReactResponseDTO;
import com.hasithmalshan.confession_form.service.PostReactService;
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
    public ResponseEntity<Void> addReactToPost(@RequestBody PostReactRequestDTO postReactRequestDTO) {
        try {
            postReactService.addReactToPost(postReactRequestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping
    public ResponseEntity<Void> removeReactFromPost(@RequestParam Long postId, @RequestParam Long userId) {
        try {
            postReactService.removeReactFromPost(postId, userId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostReactResponseDTO> getReactsForPost(@PathVariable Long postId) {
        try {
            PostReactResponseDTO responseDTO = postReactService.getReactsForPost(postId);
            if (responseDTO != null) {
                return ResponseEntity.ok(responseDTO);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}

