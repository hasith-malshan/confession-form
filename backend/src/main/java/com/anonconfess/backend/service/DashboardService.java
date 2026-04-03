package com.anonconfess.backend.service;

import com.anonconfess.backend.dto.UserDashboardResponse;

public interface DashboardService {
    UserDashboardResponse getMyDashboard();
}