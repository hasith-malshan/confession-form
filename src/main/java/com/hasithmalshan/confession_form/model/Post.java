package com.hasithmalshan.confession_form.model;

import com.hasithmalshan.confession_form.model.enums.MoodType;
import com.hasithmalshan.confession_form.model.enums.PostCategory;
import com.hasithmalshan.confession_form.model.enums.VisibilityLevel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "post")
@Getter
@Setter
@NoArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MoodType mood;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PostCategory category;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comment;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostReact> react = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VisibilityLevel visibilityLevel;
}
