package com.anonconfess.backend.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConfessionResponse {

    private Long id;
    private String name;
    private String mood;
    private String category;
    private String text;
    private LocalDateTime createdAt;
    private Long userId;
    private List<ReactionCountResponse> reactions;
    private List<CommentResponse> comments;
    private int commentCount;

}