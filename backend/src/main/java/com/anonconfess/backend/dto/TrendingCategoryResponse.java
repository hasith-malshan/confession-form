package com.anonconfess.backend.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrendingCategoryResponse {
    private String category;
    private long count;
}