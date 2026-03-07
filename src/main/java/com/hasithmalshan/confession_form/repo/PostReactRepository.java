package com.hasithmalshan.confession_form.repo;

import com.hasithmalshan.confession_form.dto.ReactionSummary;
import com.hasithmalshan.confession_form.model.PostReact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostReactRepository extends JpaRepository<PostReact, Integer> {
    @Query(
            "SELECT pr.reactType, COUNT(pr) " +
                    "FROM PostReact pr " +
                    "WHERE pr.post.id = :postId " +
                    "GROUP BY pr.reactType " +
                    "ORDER BY pr.reactType ASC"
    )
    List<ReactionSummary> countPostReactByPost(Long postId);

    void deletePostReactByPostIdAndUserId(Long postId, Long userId);
}
