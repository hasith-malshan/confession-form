package com.anonconfess.backend.service.impl;

import com.anonconfess.backend.dto.ConfessionResponse;
import com.anonconfess.backend.dto.CreateReactionRequest;
import com.anonconfess.backend.entity.Confession;
import com.anonconfess.backend.entity.Reaction;
import com.anonconfess.backend.entity.User;
import com.anonconfess.backend.repository.ConfessionRepository;
import com.anonconfess.backend.repository.ReactionRepository;
import com.anonconfess.backend.repository.UserRepository;
import com.anonconfess.backend.service.ConfessionService;
import com.anonconfess.backend.service.ReactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReactionServiceImpl implements ReactionService {

    private final ReactionRepository reactionRepository;
    private final ConfessionRepository confessionRepository;
    private final UserRepository userRepository;
    private final ConfessionService confessionService;

    @Override
    public ConfessionResponse toggleReaction(Long confessionId, CreateReactionRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Confession confession = confessionRepository.findById(confessionId)
                .orElseThrow(() -> new RuntimeException("Confession not found"));

        var existingReaction = reactionRepository
                .findByConfessionIdAndUserIdAndReactionType(confessionId, user.getId(), request.getReactionType());

        if (existingReaction.isPresent()) {
            reactionRepository.delete(existingReaction.get());
        } else {
            Reaction reaction = Reaction.builder()
                    .reactionType(request.getReactionType())
                    .createdAt(LocalDateTime.now())
                    .confession(confession)
                    .user(user)
                    .build();

            reactionRepository.save(reaction);
        }

        return confessionService.getConfessionById(confessionId);
    }
}