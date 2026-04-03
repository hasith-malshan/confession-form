package com.anonconfess.backend.controller;

import com.anonconfess.backend.dto.ConfessionResponse;
import com.anonconfess.backend.dto.CreateReactionRequest;
import com.anonconfess.backend.service.ReactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/confessions")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ReactionController {

    private final ReactionService reactionService;

    @PostMapping("/{confessionId}/reactions")
    public ConfessionResponse toggleReaction(@PathVariable Long confessionId,
                                             @Valid @RequestBody CreateReactionRequest request) {
        return reactionService.toggleReaction(confessionId, request);
    }
}