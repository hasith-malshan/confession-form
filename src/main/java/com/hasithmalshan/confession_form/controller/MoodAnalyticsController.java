package com.hasithmalshan.confession_form.controller;

import com.hasithmalshan.confession_form.dto.CommunityMoodAnalyticsDTO;
import com.hasithmalshan.confession_form.dto.UserMoodHistoryDTO;
import com.hasithmalshan.confession_form.service.MoodAnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/api/insights/mood")
@RequiredArgsConstructor
public class MoodAnalyticsController {

    private final MoodAnalyticsService moodAnalyticsService;

    /**
     * GET /api/insights/mood/community
     * Returns community-wide mood analytics (all time).
     */
    @GetMapping("/community")
    public ResponseEntity<CommunityMoodAnalyticsDTO> getCommunityMoodAnalytics() {
        CommunityMoodAnalyticsDTO analytics = moodAnalyticsService.getCommunityMoodAnalytics();
        return ResponseEntity.ok(analytics);
    }

    /**
     * GET /api/insights/mood/community?since=2025-01-01T00:00:00
     * Returns community mood analytics for posts created since the given datetime.
     */
    @GetMapping(value = "/community", params = "since")
    public ResponseEntity<CommunityMoodAnalyticsDTO> getCommunityMoodAnalyticsSince(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime since) {
        CommunityMoodAnalyticsDTO analytics = moodAnalyticsService.getCommunityMoodAnalyticsSince(since);
        return ResponseEntity.ok(analytics);
    }

    /**
     * GET /api/insights/mood/user/{userId}
     * Returns a user's mood history based on their posts.
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<UserMoodHistoryDTO> getUserMoodHistory(@PathVariable Long userId) {
        Optional<UserMoodHistoryDTO> history = moodAnalyticsService.getUserMoodHistory(userId);
        return history
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
