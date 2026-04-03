package com.anonconfess.backend.service;

import com.anonconfess.backend.dto.ConfessionResponse;
import com.anonconfess.backend.dto.CreateReactionRequest;

public interface ReactionService {
    ConfessionResponse toggleReaction(Long confessionId, CreateReactionRequest request);
}