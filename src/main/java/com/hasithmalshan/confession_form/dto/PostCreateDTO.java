package com.hasithmalshan.confession_form.dto;

import com.hasithmalshan.confession_form.model.enums.MoodType;
import com.hasithmalshan.confession_form.model.enums.PostCategory;
import com.hasithmalshan.confession_form.model.enums.VisibilityLevel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostCreateDTO {

    @NotBlank(message = "Post content is required")
    @Size(min = 10, max = 5000, message = "Post content must be between 10 and 5000 characters")
    private String content;

    @NotNull(message = "Mood is required")
    private MoodType mood;

    @NotNull(message = "Category is required")
    private PostCategory category;

    @NotNull(message = "Visibility level is required")
    private VisibilityLevel visibilityLevel;
}
