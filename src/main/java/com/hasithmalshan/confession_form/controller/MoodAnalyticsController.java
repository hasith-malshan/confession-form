package com.hasithmalshan.confession_form.controller;

import com.hasithmalshan.confession_form.dto.CommunityMoodAnalyticsDTO;
import com.hasithmalshan.confession_form.dto.UserMoodHistoryDTO;
import com.hasithmalshan.confession_form.dto.response.ApiResponse;
import com.hasithmalshan.confession_form.exception.ResourceNotFoundException;
import com.hasithmalshan.confession_form.service.MoodAnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/insights/mood")
@RequiredArgsConstructor
public class MoodAnalyticsController {

    private final MoodAnalyticsService moodAnalyticsService;

    @GetMapping("/community")
    public ResponseEntity<ApiResponse<CommunityMoodAnalyticsDTO>> getCommunityMoodAnalytics() {
        CommunityMoodAnalyticsDTO analytics = moodAnalyticsService.getCommunityMoodAnalytics();
        return ResponseEntity.ok(ApiResponse.success(analytics, "Community mood analytics retrieved successfully"));
    }

    @GetMapping(value = "/community", params = "since")
    public ResponseEntity<ApiResponse<CommunityMoodAnalyticsDTO>> getCommunityMoodAnalyticsSince(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime since) {
        CommunityMoodAnalyticsDTO analytics = moodAnalyticsService.getCommunityMoodAnalyticsSince(since);
        return ResponseEntity.ok(ApiResponse.success(analytics, "Community mood analytics retrieved successfully"));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<UserMoodHistoryDTO>> getUserMoodHistory(@PathVariable Long userId) {
        UserMoodHistoryDTO history = moodAnalyticsService.getUserMoodHistory(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Mood history", "userId", userId));
        return ResponseEntity.ok(ApiResponse.success(history, "User mood history retrieved successfully"));
    }
}
