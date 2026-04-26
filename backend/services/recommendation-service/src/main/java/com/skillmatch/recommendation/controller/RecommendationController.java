package com.skillmatch.recommendation.controller;

import com.skillmatch.recommendation.dto.response.JobRecommendationResponse;
import com.skillmatch.recommendation.security.CurrentUser;
import com.skillmatch.recommendation.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
@RequiredArgsConstructor
public class RecommendationController {

    private final RecommendationService recommendationService;

    @GetMapping("/jobs/me")
    public ResponseEntity<List<JobRecommendationResponse>> recommendJobsForMe(
            @RequestParam(defaultValue = "10") int limit,
            @AuthenticationPrincipal CurrentUser currentUser
    ) {
        return ResponseEntity.ok(
                recommendationService.recommendJobsForUser(currentUser.userId(), limit)
        );
    }

    @PostMapping("/internal/recompute/user/{userId}")
    public ResponseEntity<Void> recomputeUserEmbedding(@PathVariable Long userId) {
        recommendationService.recomputeUserEmbedding(userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/internal/recompute/job/{jobId}")
    public ResponseEntity<Void> recomputeJobEmbedding(@PathVariable Long jobId) {
        recommendationService.recomputeJobEmbedding(jobId);
        return ResponseEntity.noContent().build();
    }
}