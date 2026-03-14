package com.hasithmalshan.confession_form.util;

import com.hasithmalshan.confession_form.dto.PostDTO;
import com.hasithmalshan.confession_form.dto.PostFilterRequestDTO;
import com.hasithmalshan.confession_form.dto.PostResponseDTO;
import com.hasithmalshan.confession_form.model.Post;
import com.hasithmalshan.confession_form.model.enums.VisibilityLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;

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

    public static Specification<Post> buildSpec(PostFilterRequestDTO filter) {

        Specification<Post> spec = Specification.where(null);

        if (filter.getUserId() != null) {
            spec = spec.and(PostSpecification.hasUserId(filter.getUserId()));
        }

        if (filter.getVisibility() != null) {
            spec = spec.and(PostSpecification.hasVisibility(filter.getVisibility()));
        }
        if (filter.getCategory() != null) {
            spec = spec.and(PostSpecification.hasCategory(filter.getCategory()));
        }

        if (filter.getMood() != null) {
            spec = spec.and(PostSpecification.hasMood(filter.getMood()));
        }

        if (filter.getCreatedBefore() != null) {
            spec = spec.and(PostSpecification.createdBefore(filter.getCreatedBefore()));
        }

        if (filter.getCreatedAfter() != null) {
            spec = spec.and(PostSpecification.createdAfter(filter.getCreatedAfter()));
        }

        return spec;
    }
}
