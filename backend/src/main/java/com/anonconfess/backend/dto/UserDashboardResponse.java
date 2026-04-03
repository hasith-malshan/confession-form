package com.anonconfess.backend.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDashboardResponse {

    private String username;
    private String memberSince;
    private long totalConfessionsPosted;
    private long totalReactionsReceived;
    private long totalCommentsReceived;
    private long moodTypesUsed;
    private List<ConfessionResponse> recentConfessions;
}