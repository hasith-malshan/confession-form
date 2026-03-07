package com.hasithmalshan.confession_form.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostReactResponseDTO {
    private Long postId;
    private Long angryCount;
    private Long hahaCount;
    private Long likeCount;
    private Long loveCount;
    private Long sadCount;
    private Long wowCount;
    private Long reactCount;
}
