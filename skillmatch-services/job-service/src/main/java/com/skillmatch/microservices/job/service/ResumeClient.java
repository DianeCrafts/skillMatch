package com.skillmatch.microservices.job.service;

import com.skillmatch.microservices.job.Dto.ResumeData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class ResumeClient {

    private final RestTemplate restTemplate = new RestTemplate();

    public ResumeData getResumeByUserId(String userId) {
        String url = "http://resume-service/api/resumes/user/" + userId;
        return restTemplate.getForObject(url, ResumeData.class);
    }
}
