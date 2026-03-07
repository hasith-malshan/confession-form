package com.hasithmalshan.confession_form.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PostFilterRequestDTO {
    Long userId;
    String category;
    String mood;
    String visibility;
    LocalDateTime createdAfter;
    LocalDateTime createdBefore;
}
