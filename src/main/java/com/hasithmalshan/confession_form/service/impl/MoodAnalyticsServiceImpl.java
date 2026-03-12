package com.hasithmalshan.confession_form.service.impl;

import com.hasithmalshan.confession_form.dto.CommunityMoodAnalyticsDTO;
import com.hasithmalshan.confession_form.dto.MoodStatDTO;
import com.hasithmalshan.confession_form.dto.UserMoodHistoryDTO;
import com.hasithmalshan.confession_form.dto.UserMoodHistoryEntryDTO;
import com.hasithmalshan.confession_form.model.Post;
import com.hasithmalshan.confession_form.model.enums.MoodType;
import com.hasithmalshan.confession_form.repo.PostRepository;
import com.hasithmalshan.confession_form.repo.UserRepository;
import com.hasithmalshan.confession_form.service.MoodAnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MoodAnalyticsServiceImpl implements MoodAnalyticsService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Override
    public CommunityMoodAnalyticsDTO getCommunityMoodAnalytics() {
        List<Object[]> results = postRepository.countPostsByMood();
        return buildCommunityMoodAnalytics(results);
    }

    @Override
    public CommunityMoodAnalyticsDTO getCommunityMoodAnalyticsSince(LocalDateTime since) {
        List<Object[]> results = postRepository.countPostsByMoodSince(since);
        return buildCommunityMoodAnalytics(results);
    }

    @Override
    public Optional<UserMoodHistoryDTO> getUserMoodHistory(Long userId) {
        if (!userRepository.existsById(userId)) {
            return Optional.empty();
        }
        List<Post> posts = postRepository.findByUserIdOrderByCreatedAtDesc(userId);
        List<UserMoodHistoryEntryDTO> entries = posts.stream()
                .map(p -> new UserMoodHistoryEntryDTO(
                        p.getId(),
                        p.getMood().name(),
                        p.getCreatedAt()
                ))
                .collect(Collectors.toList());
        UserMoodHistoryDTO dto = new UserMoodHistoryDTO(userId, entries);
        return Optional.of(dto);
    }

    private CommunityMoodAnalyticsDTO buildCommunityMoodAnalytics(List<Object[]> results) {
        List<MoodStatDTO> stats = new ArrayList<>();
        long totalPosts = 0;

        for (Object[] row : results) {
            MoodType mood = (MoodType) row[0];
            Long count = (Long) row[1];
            stats.add(new MoodStatDTO(mood.name(), count));
            totalPosts += count;
        }

        return new CommunityMoodAnalyticsDTO(stats, totalPosts);
    }
}
