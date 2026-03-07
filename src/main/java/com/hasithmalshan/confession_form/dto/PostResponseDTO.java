package com.hasithmalshan.confession_form.dto;

import com.hasithmalshan.confession_form.model.enums.VisibilityLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostResponseDTO {
    private Long id;
    private Long userId;
    private String username;
    private String content;
    private String mood;
    private String category;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private VisibilityLevel visibilityLevel;
}

