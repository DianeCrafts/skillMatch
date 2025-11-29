package com.skillmatch.microservices.job.service;

import com.skillmatch.microservices.job.Dto.ResumeDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class ResumeClient {

    private final RestTemplate restTemplate = new RestTemplate();

    public ResumeDTO getResumeByUserId(String token, Long userId) {

        String url = "http://localhost:8082/api/resumes/user/" + userId;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<ResumeDTO> response = restTemplate.exchange(url, HttpMethod.GET, entity, ResumeDTO.class);

        return response.getBody();
    }

}
