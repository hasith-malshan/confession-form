package com.hasithmalshan.confession_form.service;

import com.hasithmalshan.confession_form.dto.CommunityMoodAnalyticsDTO;
import com.hasithmalshan.confession_form.dto.UserMoodHistoryDTO;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Optional;

public interface MoodAnalyticsService {
    /**
     * Get community mood analytics - counts of posts per mood across all posts.
     */
    CommunityMoodAnalyticsDTO getCommunityMoodAnalytics();

    /**
     * Get community mood analytics for posts created since the given date.
     *
     * @param since Only include posts created on or after this datetime
     */
    CommunityMoodAnalyticsDTO getCommunityMoodAnalyticsSince(LocalDateTime since);

    /**
     * Get a user's mood history based on their posts (chronological, most recent first).
     *
     * @param userId the user's ID
     * @return mood history or empty if user not found or has no posts
     */
    Optional<UserMoodHistoryDTO> getUserMoodHistory(Long userId);
}
