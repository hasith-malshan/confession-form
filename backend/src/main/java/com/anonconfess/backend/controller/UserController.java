package com.anonconfess.backend.controller;

import com.anonconfess.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;

    @GetMapping("/count")
    public long getUserCount() {
        return userService.getUserCount();
    }
}