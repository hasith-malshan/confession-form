package com.anonconfess.backend.repository;
import com.anonconfess.backend.dto.TrendingCategoryResponse;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

import com.anonconfess.backend.entity.Confession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfessionRepository extends JpaRepository<Confession, Long> {

    Page<Confession> findByMoodIgnoreCase(String mood, Pageable pageable);
    Page<Confession> findByCategoryIgnoreCase(String category, Pageable pageable);

    Page<Confession> findByMoodIgnoreCaseAndCategoryIgnoreCase(String mood, String category, Pageable pageable);

    @Query("""
    SELECT new com.anonconfess.backend.dto.TrendingCategoryResponse(c.category, COUNT(c))
    FROM Confession c
    WHERE c.createdAt >= :startOfWeek
    GROUP BY c.category
    ORDER BY COUNT(c) DESC
""")
    List<TrendingCategoryResponse> findTrendingCategoriesThisWeek(@Param("startOfWeek") LocalDateTime startOfWeek);
    @Query("""
    SELECT c.mood, COUNT(c)
    FROM Confession c
    WHERE c.createdAt >= :startOfDay
    GROUP BY c.mood
""")
    List<Object[]> findTodayMoodCounts(@Param("startOfDay") LocalDateTime startOfDay);
    long countByCreatedAtGreaterThanEqual(LocalDateTime startOfDay);
    long countByUserId(Long userId);

    List<Confession> findTop5ByUserIdOrderByCreatedAtDesc(Long userId);

    @org.springframework.data.jpa.repository.Query("SELECT COUNT(DISTINCT c.mood) FROM Confession c WHERE c.user.id = :userId")
    long countDistinctMoodsByUserId(@org.springframework.data.repository.query.Param("userId") Long userId);

}