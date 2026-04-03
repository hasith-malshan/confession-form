package com.anonconfess.backend.service.impl;

import com.anonconfess.backend.repository.UserRepository;
import com.anonconfess.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public long getUserCount() {
        return userRepository.count();
    }
}