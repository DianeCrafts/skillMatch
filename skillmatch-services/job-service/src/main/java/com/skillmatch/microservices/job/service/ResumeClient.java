package com.skillmatch.microservices.job.service;

import com.google.gson.Gson;
import com.skillmatch.microservices.job.dto.ResumeDTO;
import com.skillmatch.microservices.job.dto.ai.ResumeEmbeddingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
@Component
@RequiredArgsConstructor
public class ResumeClient {

    private final WebClient resumeWebClient;

    public ResumeDTO getResumeByUserId(String token, Long userId) {

        return resumeWebClient.get()
                .uri("/api/resumes/user/{userId}", userId)
                .header("Authorization", token)
                .retrieve()
                .bodyToMono(ResumeDTO.class)
                .block();
    }

    public String getUserName(String token, Long userId) {

        ResumeDTO resume = resumeWebClient.get()
                .uri("/api/resumes/user/{userId}", userId)
                .header("Authorization", token)
                .retrieve()
                .bodyToMono(ResumeDTO.class)
                .block();

        return resume != null ? resume.name() : "Unknown";
    }

    public float[] getUserEmbedding(Long userId, String token) {

        ResumeEmbeddingResponse response = resumeWebClient.get()
                .uri("/api/resumes/user/{userId}/embedding", userId)
                .header("Authorization", token)
                .retrieve()
                .bodyToMono(ResumeEmbeddingResponse.class)
                .block();

        return response.vector();
    }
}
