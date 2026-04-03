package com.anonconfess.backend.service.impl;

import com.anonconfess.backend.dto.ConfessionResponse;
import com.anonconfess.backend.dto.UserDashboardResponse;
import com.anonconfess.backend.entity.Confession;
import com.anonconfess.backend.entity.User;
import com.anonconfess.backend.repository.CommentRepository;
import com.anonconfess.backend.repository.ConfessionRepository;
import com.anonconfess.backend.repository.ReactionRepository;
import com.anonconfess.backend.repository.UserRepository;
import com.anonconfess.backend.service.ConfessionService;
import com.anonconfess.backend.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final UserRepository userRepository;
    private final ConfessionRepository confessionRepository;
    private final ReactionRepository reactionRepository;
    private final CommentRepository commentRepository;
    private final ConfessionService confessionService;

    @Override
    public UserDashboardResponse getMyDashboard() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        long totalConfessionsPosted = confessionRepository.countByUserId(user.getId());
        long totalReactionsReceived = reactionRepository.countReactionsReceivedByUserId(user.getId());
        long totalCommentsReceived = commentRepository.countCommentsReceivedByUserId(user.getId());
        long moodTypesUsed = confessionRepository.countDistinctMoodsByUserId(user.getId());

        List<Confession> recentConfessionEntities = confessionRepository.findTop5ByUserIdOrderByCreatedAtDesc(user.getId());

        List<ConfessionResponse> recentConfessions = recentConfessionEntities.stream()
                .map(confession -> confessionService.getConfessionById(confession.getId()))
                .toList();

        String memberSince = user.getCreatedAt() != null
                ? user.getCreatedAt().format(DateTimeFormatter.ofPattern("MMM yyyy"))
                : "";

        return UserDashboardResponse.builder()
                .username(user.getUsername())
                .memberSince(memberSince)
                .totalConfessionsPosted(totalConfessionsPosted)
                .totalReactionsReceived(totalReactionsReceived)
                .totalCommentsReceived(totalCommentsReceived)
                .moodTypesUsed(moodTypesUsed)
                .recentConfessions(recentConfessions)
                .build();
    }
}