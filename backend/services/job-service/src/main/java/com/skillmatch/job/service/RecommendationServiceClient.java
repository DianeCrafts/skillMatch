package com.skillmatch.job.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class RecommendationServiceClient {

    private final RestClient recommendationRestClient;

    public void recomputeJobEmbedding(Long jobId) {
        try {
            recommendationRestClient.post()
                    .uri("/api/recommendations/internal/recompute/job/{jobId}", jobId)
                    .retrieve()
                    .toBodilessEntity();
        } catch (Exception e) {
            e.printStackTrace();
            // Do not fail job publishing if recommendation update fails
        }
    }
}