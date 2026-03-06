package com.hasithmalshan.confession_form.repo;

import com.hasithmalshan.confession_form.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findByMobileNo(String mobileNo);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByMobileNo(String mobileNo);
}

