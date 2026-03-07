package com.hasithmalshan.confession_form.dto;

import com.hasithmalshan.confession_form.model.enums.ReactType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostReactRequestDTO {
    private long userId;
    private long postId;
    private ReactType reactType;
}
