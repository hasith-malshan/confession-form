package com.hasithmalshan.confession_form.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {
    private Long id;
    private String content;
    private Long userId;
    private Long postId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

