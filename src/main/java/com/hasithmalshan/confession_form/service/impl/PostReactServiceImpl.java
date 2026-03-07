package com.hasithmalshan.confession_form.service.impl;

import com.hasithmalshan.confession_form.dto.PostReactDTO;
import com.hasithmalshan.confession_form.dto.PostReactRequestDTO;
import com.hasithmalshan.confession_form.dto.PostReactResponseDTO;
import com.hasithmalshan.confession_form.dto.ReactionSummary;
import com.hasithmalshan.confession_form.model.PostReact;
import com.hasithmalshan.confession_form.repo.PostReactRepository;
import com.hasithmalshan.confession_form.repo.PostRepository;
import com.hasithmalshan.confession_form.repo.UserRepository;
import com.hasithmalshan.confession_form.service.PostReactService;
import com.hasithmalshan.confession_form.service.PostService;
import com.hasithmalshan.confession_form.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PostReactServiceImpl implements PostReactService {

    private final PostReactRepository postReactRepository;
    public final UserRepository userRepository;
    public final PostRepository postRepository;

    @Override
    public void addReactToPost(PostReactRequestDTO postReactRequestDTO) {
        PostReact postReact = new PostReact();
        postReact.setPost(postRepository.getReferenceById(postReactRequestDTO.getPostId()));
        postReact.setUser(userRepository.getReferenceById(postReactRequestDTO.getUserId()));
        postReact.setReactType(postReactRequestDTO.getReactType());
        postReactRepository.save(postReact);
    }

    @Override
    public void removeReactFromPost(Long postId, Long userId) {
            postReactRepository.deletePostReactByPostIdAndUserId(postId, userId);
    }

    @Override
    public PostReactResponseDTO getReactsForPost(Long postId) {
        List<ReactionSummary> result = postReactRepository.countPostReactByPost(postId);

        if (result.isEmpty()) return null;

        PostReactResponseDTO postReactResponseDTO = new PostReactResponseDTO();
        postReactResponseDTO.setPostId(0L);
        postReactResponseDTO.setAngryCount(0L);
        postReactResponseDTO.setHahaCount(0L);
        postReactResponseDTO.setLikeCount(0L);
        postReactResponseDTO.setLoveCount(0L);
        postReactResponseDTO.setSadCount(0L);
        postReactResponseDTO.setWowCount(0L);

        for (ReactionSummary reactionSummary : result) {
            switch (reactionSummary.getType()) {
                case "LIKE" -> postReactResponseDTO.setLikeCount(reactionSummary.getCount());
                case "LOVE" -> postReactResponseDTO.setLoveCount(reactionSummary.getCount());
                case "HAHA" -> postReactResponseDTO.setHahaCount(reactionSummary.getCount());
                case "WOW" -> postReactResponseDTO.setWowCount(reactionSummary.getCount());
                case "SAD" -> postReactResponseDTO.setSadCount(reactionSummary.getCount());
                case "ANGRY" -> postReactResponseDTO.setAngryCount(reactionSummary.getCount());
            }
        }

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
