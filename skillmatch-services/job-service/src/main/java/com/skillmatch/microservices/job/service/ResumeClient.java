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

    private final RestTemplate restTemplate = new RestTemplate();
    private final WebClient.Builder webClientBuilder;

    public ResumeDTO getResumeByUserId(String token, Long userId) {

        String url = "http://localhost:8082/api/resumes/user/" + userId;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<ResumeDTO> response = restTemplate.exchange(url, HttpMethod.GET, entity, ResumeDTO.class);

        return response.getBody();
    }

    public String getUserName(String token, Long userId) {
        String url = "http://localhost:8082/api/resumes/user/" + userId;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResumeDTO resume = restTemplate.exchange(url, HttpMethod.GET, entity, ResumeDTO.class).getBody();
        return resume != null ? resume.name() : "Unknown";
    }

    public float[] getUserEmbedding(Long userId, String token) {

        ResumeEmbeddingResponse response = webClientBuilder.baseUrl("http://localhost:8082")
                .build()
                .get()
                .uri("/api/resumes/user/{userId}/embedding", userId)
                .header("Authorization", token)
                .retrieve()
                .bodyToMono(ResumeEmbeddingResponse.class)
                .block();

        return response.vector();
    }

}
