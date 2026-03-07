package com.hasithmalshan.confession_form.service.impl;

import com.hasithmalshan.confession_form.dto.PostReactDTO;
import com.hasithmalshan.confession_form.dto.PostReactResponseDTO;
import com.hasithmalshan.confession_form.dto.ReactionSummary;
import com.hasithmalshan.confession_form.repo.PostReactRepository;
import com.hasithmalshan.confession_form.service.PostReactService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PostReactServiceImpl implements PostReactService {

    private final PostReactRepository postReactRepository;

    @Override
    public void addReactToPost(Long postId, Long userId, String reactType) {

    }

    @Override
    public void removeReactFromPost(Long postId, Long userId) {

    }

    @Override
    public PostReactResponseDTO getReactsForPost(Long postId) {
        List<ReactionSummary> result = postReactRepository.countPostReactByPost(postId);
        PostReactResponseDTO postReactResponseDTO = new PostReactResponseDTO();
        postReactResponseDTO.setAngryCount(result.get(0).getCount());
        postReactResponseDTO.setHahaCount(result.get(1).getCount());
        postReactResponseDTO.setLikeCount(result.get(2).getCount());
        postReactResponseDTO.setLoveCount(result.get(3).getCount());
        postReactResponseDTO.setSadCount(result.get(4).getCount());
        postReactResponseDTO.setWowCount(result.get(5).getCount());

        long count = 0L;
        for (ReactionSummary reactionSummary : result) {
            count += reactionSummary.getCount();
        }
        postReactResponseDTO.setReactCount(count);

        return postReactResponseDTO;
    }

    @Override
    public PostReactDTO toDto(Long postId, Long userId, String reactType) {
        return null;
    }
}
