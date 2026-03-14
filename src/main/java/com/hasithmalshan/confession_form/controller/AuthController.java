package com.hasithmalshan.confession_form.controller;

import com.hasithmalshan.confession_form.dto.AuthResponse;
import com.hasithmalshan.confession_form.dto.LoginRequest;
import com.hasithmalshan.confession_form.dto.UserRegistrationDTO;
import com.hasithmalshan.confession_form.dto.response.ApiResponse;
import com.hasithmalshan.confession_form.model.User;
import com.hasithmalshan.confession_form.security.JwtUtil;
import com.hasithmalshan.confession_form.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = userService.getUserByUsernameEntity(request.getUsername());
        List<String> roles = List.of("ROLE_" + user.getRole().name());

        String token = jwtUtil.generateToken(user.getId(), user.getUsername(), roles);

        AuthResponse authResponse = new AuthResponse(
                token,
                "Bearer",
                user.getId(),
                user.getUsername(),
                roles
        );

        return ResponseEntity.ok(ApiResponse.success(authResponse, "Login successful"));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody UserRegistrationDTO registrationDTO) {
        User user = userService.registerUser(registrationDTO);

        List<String> roles = List.of("ROLE_" + user.getRole().name());
        String token = jwtUtil.generateToken(user.getId(), user.getUsername(), roles);

        AuthResponse authResponse = new AuthResponse(
                token,
                "Bearer",
                user.getId(),
                user.getUsername(),
                roles
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(authResponse, "Registration successful"));
    }
}
