package com.anonconfess.backend.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentResponse {

    private Long id;
    private String text;
    private LocalDateTime createdAt;
    private Long userId;
    private String username;
}