package com.hasithmalshan.confession_form.dto;

import com.hasithmalshan.confession_form.model.enums.ReactType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostReactRequestDTO {

    @NotNull(message = "User ID is required")
    @Positive(message = "User ID must be a positive number")
    private Long userId;

    @NotNull(message = "Post ID is required")
    @Positive(message = "Post ID must be a positive number")
    private Long postId;

    @NotNull(message = "React type is required")
    private ReactType reactType;
}
