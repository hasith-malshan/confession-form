package com.anonconfess.backend.repository;

import com.anonconfess.backend.entity.Reaction;
import com.anonconfess.backend.entity.ReactionType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReactionRepository extends JpaRepository<Reaction, Long> {

    long countByConfessionIdAndReactionType(Long confessionId, ReactionType reactionType);

    Optional<Reaction> findByConfessionIdAndUserIdAndReactionType(Long confessionId, Long userId, ReactionType reactionType);


    @org.springframework.data.jpa.repository.Query("""
    SELECT COUNT(r)
    FROM Reaction r
    WHERE r.confession.user.id = :userId
""")
    long countReactionsReceivedByUserId(@org.springframework.data.repository.query.Param("userId") Long userId);
}