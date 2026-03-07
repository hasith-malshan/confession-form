package com.hasithmalshan.confession_form.util;

import com.hasithmalshan.confession_form.dto.PostDTO;
import com.hasithmalshan.confession_form.dto.PostResponseDTO;
import com.hasithmalshan.confession_form.model.enums.VisibilityLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@NoArgsConstructor
public final class PostUtils {

    public static Page<PostResponseDTO> ensureAnonymity(Page<PostDTO> posts) {
        Page<PostResponseDTO> map = posts.map(PostUtils::toPostResponseDTO);
        map.forEach(post -> {
            if (post.getVisibilityLevel() == VisibilityLevel.ANONYMOUS) {
                post.setUsername("Anonymous User");
            }
        });
        return map;
    }

    public static PostResponseDTO toPostResponseDTO(PostDTO postDTO) {
        PostResponseDTO responseDTO = new PostResponseDTO();
        responseDTO.setId(postDTO.getId());
        responseDTO.setUserId(postDTO.getUserId());
        responseDTO.setContent(postDTO.getContent());
        responseDTO.setMood(postDTO.getMood());
        responseDTO.setCategory(postDTO.getCategory());
        responseDTO.setCreatedAt(postDTO.getCreatedAt());
        responseDTO.setUpdatedAt(postDTO.getUpdatedAt());
        responseDTO.setVisibilityLevel(postDTO.getVisibilityLevel());
        return responseDTO;
    }
}
