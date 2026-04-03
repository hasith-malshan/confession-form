package com.anonconfess.backend.service.impl;

import com.anonconfess.backend.dto.*;
import com.anonconfess.backend.entity.Confession;
import com.anonconfess.backend.entity.ReactionType;
import com.anonconfess.backend.entity.User;
import com.anonconfess.backend.repository.ConfessionRepository;
import com.anonconfess.backend.repository.ReactionRepository;
import com.anonconfess.backend.repository.UserRepository;
import com.anonconfess.backend.service.ConfessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import com.anonconfess.backend.dto.TrendingCategoryResponse;
import java.time.DayOfWeek;
import java.time.LocalDate;

import com.anonconfess.backend.dto.MoodDistributionResponse;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class ConfessionServiceImpl implements ConfessionService {

    private final ConfessionRepository confessionRepository;
    private final UserRepository userRepository;
    private final ReactionRepository reactionRepository;

    @Override
    public ConfessionResponse createConfession(CreateConfessionRequest request) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Confession confession = Confession.builder()
                .name(request.getName())
                .mood(request.getMood())
                .category(request.getCategory())
                .text(request.getText())
                .createdAt(LocalDateTime.now())
                .user(user)
                .build();

        Confession saved = confessionRepository.save(confession);

        return mapToResponse(saved);
    }



    private ConfessionResponse mapToResponse(Confession confession) {
        List<ReactionCountResponse> reactionCounts = List.of(
                ReactionCountResponse.builder()
                        .reactionType("FEEL_THIS")
                        .count(reactionRepository.countByConfessionIdAndReactionType(confession.getId(), ReactionType.FEEL_THIS))
                        .build(),
                ReactionCountResponse.builder()
                        .reactionType("FUNNY")
                        .count(reactionRepository.countByConfessionIdAndReactionType(confession.getId(), ReactionType.FUNNY))
                        .build(),
                ReactionCountResponse.builder()
                        .reactionType("STAY_STRONG")
                        .count(reactionRepository.countByConfessionIdAndReactionType(confession.getId(), ReactionType.STAY_STRONG))
                        .build(),
                ReactionCountResponse.builder()
                        .reactionType("BEEN_THERE")
                        .count(reactionRepository.countByConfessionIdAndReactionType(confession.getId(), ReactionType.BEEN_THERE))
                        .build()
        );

        List<CommentResponse> commentResponses = confession.getComments().stream()
                .map(comment -> CommentResponse.builder()
                        .id(comment.getId())
                        .text(comment.getText())
                        .createdAt(comment.getCreatedAt())
                        .userId(comment.getUser() != null ? comment.getUser().getId() : null)
                        .username(comment.getUser() != null ? comment.getUser().getUsername() : "Anonymous")
                        .build())
                .toList();

        return ConfessionResponse.builder()
                .id(confession.getId())
                .name(confession.getName())
                .mood(confession.getMood())
                .category(confession.getCategory())
                .text(confession.getText())
                .createdAt(confession.getCreatedAt())
                .userId(confession.getUser() != null ? confession.getUser().getId() : null)
                .reactions(reactionCounts)
                .comments(commentResponses)
                .commentCount(commentResponses.size())
                .build();
    }
    @Override
    public void deleteConfession(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Confession confession = confessionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Confession not found"));

        if (!confession.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You can delete only your own confession");
        }

        confessionRepository.delete(confession);
    }

    @Override
    public ConfessionResponse updateConfession(Long id, UpdateConfessionRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Confession confession = confessionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Confession not found"));

        if (!confession.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You can edit only your own confession");
        }

        confession.setText(request.getText());

        Confession updated = confessionRepository.save(confession);

        return mapToResponse(updated);
    }
    @Override
    public ConfessionResponse getConfessionById(Long id) {
        Confession confession = confessionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Confession not found"));

        return mapToResponse(confession);
    }
    @Override
    public Page<ConfessionResponse> getAllConfessions(String mood, String category, Pageable pageable) {
        Page<Confession> confessions;

        boolean hasMood = mood != null && !mood.isBlank();
        boolean hasCategory = category != null && !category.isBlank();

        if (hasMood && hasCategory) {
            confessions = confessionRepository.findByMoodIgnoreCaseAndCategoryIgnoreCase(mood, category, pageable);
        } else if (hasMood) {
            confessions = confessionRepository.findByMoodIgnoreCase(mood, pageable);
        } else if (hasCategory) {
            confessions = confessionRepository.findByCategoryIgnoreCase(category, pageable);
        } else {
            confessions = confessionRepository.findAll(pageable);
        }

        return confessions.map(this::mapToResponse);
    }

    @Override
    public List<TrendingCategoryResponse> getTrendingCategoriesThisWeek() {
        LocalDate today = LocalDate.now();
        LocalDate startOfWeekDate = today.with(DayOfWeek.MONDAY);
        LocalDateTime startOfWeek = startOfWeekDate.atStartOfDay();

        List<TrendingCategoryResponse> result = confessionRepository.findTrendingCategoriesThisWeek(startOfWeek);

        return result.stream().limit(5).toList();
    }

    @Override
    public List<MoodDistributionResponse> getTodayMoodDistribution() {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();

        List<Object[]> rawCounts = confessionRepository.findTodayMoodCounts(startOfDay);
        long totalToday = confessionRepository.countByCreatedAtGreaterThanEqual(startOfDay);

        List<MoodDistributionResponse> result = new ArrayList<>();

        for (Object[] row : rawCounts) {
            String mood = (String) row[0];
            long count = (Long) row[1];

            double percentage = totalToday == 0 ? 0 : (count * 100.0) / totalToday;

            result.add(MoodDistributionResponse.builder()
                    .mood(mood)
                    .count(count)
                    .percentage(percentage)
                    .build());
        }

        return result;
    }
}