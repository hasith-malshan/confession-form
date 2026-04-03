package com.anonconfess.backend.repository;

import com.anonconfess.backend.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByConfessionIdOrderByCreatedAtAsc(Long confessionId);
    @org.springframework.data.jpa.repository.Query("""
    SELECT COUNT(c)
    FROM Comment c
    WHERE c.confession.user.id = :userId
""")
    long countCommentsReceivedByUserId(@org.springframework.data.repository.query.Param("userId") Long userId);
}