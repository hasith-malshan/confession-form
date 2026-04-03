package com.anonconfess.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String text;

    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "confession_id")
    @JsonIgnore
    private Confession confession;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}