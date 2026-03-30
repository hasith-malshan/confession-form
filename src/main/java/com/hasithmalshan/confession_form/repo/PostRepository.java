package com.hasithmalshan.confession_form.repo;

import com.hasithmalshan.confession_form.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, JpaSpecificationExecutor<Post> {
    List<Post> findByUserId(Long userId);

    Page<Post> findAll(Specification<Post> specification, Pageable pageable);

    List<Post> findByUserIdOrderByCreatedAtDesc(Long userId);

    @Query("SELECT p.mood, COUNT(p) FROM Post p GROUP BY p.mood")
    List<Object[]> countPostsByMood();

    @Query("SELECT p.mood, COUNT(p) FROM Post p WHERE p.createdAt >= :since GROUP BY p.mood")
    List<Object[]> countPostsByMoodSince(LocalDateTime since);

    @Query(value = "SELECT p.* FROM post p " +
            "LEFT JOIN post_react pr ON pr.post_id = p.id " +
            "LEFT JOIN comment c ON c.post_id = p.id " +
            "WHERE p.created_at >= :since " +
            "GROUP BY p.id " +
            "ORDER BY (COUNT(DISTINCT pr.id) + COUNT(DISTINCT c.id) * 2) DESC",
            nativeQuery = true)
    List<Post> findTrendingPosts(@Param("since") LocalDateTime since, Pageable pageable);
}

