package com.anonconfess.backend.service.impl;

import com.anonconfess.backend.dto.ConfessionResponse;
import com.anonconfess.backend.dto.CreateCommentRequest;
import com.anonconfess.backend.entity.Comment;
import com.anonconfess.backend.entity.Confession;
import com.anonconfess.backend.entity.User;
import com.anonconfess.backend.repository.CommentRepository;
import com.anonconfess.backend.repository.ConfessionRepository;
import com.anonconfess.backend.repository.UserRepository;
import com.anonconfess.backend.service.CommentService;
import com.anonconfess.backend.service.ConfessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final ConfessionRepository confessionRepository;
    private final UserRepository userRepository;
    private final ConfessionService confessionService;

    @Override
    public ConfessionResponse addComment(Long confessionId, CreateCommentRequest request) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Confession confession = confessionRepository.findById(confessionId)
                .orElseThrow(() -> new RuntimeException("Confession not found"));

        Comment comment = Comment.builder()
                .text(request.getText())
                .createdAt(LocalDateTime.now())
                .confession(confession)
                .user(user)
                .build();

        commentRepository.save(comment);

        return confessionService.getConfessionById(confessionId);
    }
}