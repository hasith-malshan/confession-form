package com.anonconfess.backend.controller;

import com.anonconfess.backend.dto.ConfessionResponse;
import com.anonconfess.backend.dto.CreateCommentRequest;
import com.anonconfess.backend.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/confessions")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{confessionId}/comments")
    public ConfessionResponse addComment(@PathVariable Long confessionId,
                                         @Valid @RequestBody CreateCommentRequest request) {
        return commentService.addComment(confessionId, request);
    }
}