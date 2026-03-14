package com.hasithmalshan.confession_form.security;

import com.hasithmalshan.confession_form.model.Comment;
import com.hasithmalshan.confession_form.model.Post;
import com.hasithmalshan.confession_form.repo.CommentRepository;
import com.hasithmalshan.confession_form.repo.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

/**
 * Service for authorization checks used in @PreAuthorize SpEL expressions.
 * Provides methods to verify resource ownership and access permissions.
 */
@Service("authz")
@RequiredArgsConstructor
public class AuthorizationService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    /**
     * Get the current authenticated user's ID.
     */
    public Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getDetails() == null) {
            return null;
        }
        if (authentication.getDetails() instanceof JwtAuthDetails details) {
            return details.getUserId();
        }
        return null;
    }

    /**
     * Check if the current user is the owner of the specified user account.
     */
    public boolean isCurrentUser(Long userId) {
        Long currentUserId = getCurrentUserId();
        return currentUserId != null && currentUserId.equals(userId);
    }

    /**
     * Check if the current user owns the specified post.
     */
    public boolean isPostOwner(Long postId) {
        Long currentUserId = getCurrentUserId();
        if (currentUserId == null || postId == null) {
            return false;
        }
        Optional<Post> post = postRepository.findById(postId);
        return post.map(p -> Objects.equals(p.getUser().getId(), currentUserId)).orElse(false);
    }

    /**
     * Check if the current user owns the specified comment.
     */
    public boolean isCommentOwner(Long commentId) {
        Long currentUserId = getCurrentUserId();
        if (currentUserId == null || commentId == null) {
            return false;
        }
        Optional<Comment> comment = commentRepository.findById(commentId);
        return comment.map(c -> c.getUser() != null && Objects.equals(c.getUser().getId(), currentUserId)).orElse(false);
    }

    /**
     * Check if the current user owns the reaction on the specified post.
     */
    public boolean isReactOwner(Long postId, Long userId) {
        Long currentUserId = getCurrentUserId();
        return currentUserId != null && currentUserId.equals(userId);
    }

    /**
     * Check if the current user has ADMIN role.
     */
    public boolean isAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return false;
        }
        return authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }
}
