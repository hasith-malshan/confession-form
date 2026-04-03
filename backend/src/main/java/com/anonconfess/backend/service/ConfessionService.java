package com.anonconfess.backend.service;

import com.anonconfess.backend.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ConfessionService {
    ConfessionResponse createConfession(CreateConfessionRequest request);
    Page<ConfessionResponse> getAllConfessions(String mood,String category,Pageable pageable);
    void deleteConfession(Long id);
    ConfessionResponse updateConfession(Long id, UpdateConfessionRequest request);
    ConfessionResponse getConfessionById(Long id);
    java.util.List<TrendingCategoryResponse> getTrendingCategoriesThisWeek();
    java.util.List<MoodDistributionResponse> getTodayMoodDistribution();
}