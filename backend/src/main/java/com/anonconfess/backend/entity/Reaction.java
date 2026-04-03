package com.anonconfess.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "reactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ReactionType reactionType;

    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "confession_id")
    @JsonIgnore
    private Confession confession;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}