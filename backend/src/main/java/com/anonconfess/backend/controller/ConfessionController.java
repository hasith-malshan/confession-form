package com.anonconfess.backend.controller;

import com.anonconfess.backend.dto.*;
import com.anonconfess.backend.service.ConfessionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/confessions")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ConfessionController {

    private final ConfessionService confessionService;

    @PostMapping
    public ConfessionResponse createConfession(@Valid @RequestBody CreateConfessionRequest request) {
        return confessionService.createConfession(request);
    }

    @GetMapping
    public Page<ConfessionResponse> getAllConfessions(
            @RequestParam(required = false) String mood,
            @RequestParam(required = false) String category,
            Pageable pageable) {
        return confessionService.getAllConfessions(mood, category, pageable);
    }
    @DeleteMapping("/{id}")
    public void deleteConfession(@PathVariable Long id) {
        confessionService.deleteConfession(id);
    }

    @PutMapping("/{id}")
    public ConfessionResponse updateConfession(@PathVariable Long id,
                                               @Valid @RequestBody UpdateConfessionRequest request) {
        return confessionService.updateConfession(id, request);
    }
    @GetMapping("/trending-categories/week")
    public List<TrendingCategoryResponse> getTrendingCategoriesThisWeek() {
        return confessionService.getTrendingCategoriesThisWeek();
    }

    @GetMapping("/mood-distribution/today")
    public List<MoodDistributionResponse> getTodayMoodDistribution() {
        return confessionService.getTodayMoodDistribution();
    }
}