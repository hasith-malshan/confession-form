package com.anonconfess.backend.service;

import com.anonconfess.backend.dto.AuthResponse;
import com.anonconfess.backend.dto.LoginRequest;
import com.anonconfess.backend.dto.RegisterRequest;
import com.anonconfess.backend.dto.UserResponse;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
    UserResponse getCurrentUser();
}