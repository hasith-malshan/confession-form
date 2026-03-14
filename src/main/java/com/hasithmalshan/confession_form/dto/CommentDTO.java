package com.hasithmalshan.confession_form.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

    @NotBlank(message = "Comment content is required")
    @Size(min = 1, max = 1000, message = "Comment must be between 1 and 1000 characters")
    private String content;

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Post ID is required")
    private Long postId;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

