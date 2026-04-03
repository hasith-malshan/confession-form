package com.anonconfess.backend.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MoodDistributionResponse {
    private String mood;
    private long count;
    private double percentage;
}