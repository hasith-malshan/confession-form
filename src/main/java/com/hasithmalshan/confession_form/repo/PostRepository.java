package com.hasithmalshan.confession_form.repo;

import com.hasithmalshan.confession_form.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, JpaSpecificationExecutor<Post> {
    List<Post> findByUserId(Long userId);

    Page<Post> findAll(Pageable pageable);

    List<Post> findByUserIdOrderByCreatedAtDesc(Long userId);
}

