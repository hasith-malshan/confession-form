package com.hasithmalshan.confession_form.service;

import com.hasithmalshan.confession_form.dto.PostReactDTO;
import com.hasithmalshan.confession_form.dto.PostReactResponseDTO;
import org.springframework.stereotype.Service;

@Service
public interface PostReactService {
    void addReactToPost(Long postId, Long userId, String reactType);
    void removeReactFromPost(Long postId, Long userId);
    PostReactResponseDTO getReactsForPost(Long postId);
    PostReactDTO toDto(Long postId, Long userId, String reactType);
}
