package com.anonconfess.backend.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReactionCountResponse {
    private String reactionType;
    private long count;
}