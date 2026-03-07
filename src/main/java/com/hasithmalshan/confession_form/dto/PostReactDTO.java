package com.hasithmalshan.confession_form.dto;

import com.hasithmalshan.confession_form.model.Post;
import com.hasithmalshan.confession_form.model.User;
import com.hasithmalshan.confession_form.model.enums.ReactType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class PostReactDTO {
    private Long id;
    private ReactType reactType;
    private User user;
    private Post post;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
