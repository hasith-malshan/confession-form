package com.anonconfess.backend.controller;

import com.anonconfess.backend.dto.UserDashboardResponse;
import com.anonconfess.backend.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/me")
    public UserDashboardResponse getMyDashboard() {
        return dashboardService.getMyDashboard();
    }
}