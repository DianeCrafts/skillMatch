package com.skillmatch.application.service.impl;

import com.skillmatch.application.dto.response.JobSummaryResponse;
import com.skillmatch.application.exception.BadRequestException;
import com.skillmatch.application.exception.ResourceNotFoundException;
import com.skillmatch.application.service.JobClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class JobClientServiceImpl implements JobClientService {

    private final RestTemplate restTemplate;

    @Value("${services.job-service.url}")
    private String jobServiceUrl;

    @Override
    public JobSummaryResponse getJobById(Long jobId, String authorizationHeader) {
        String url = jobServiceUrl + "/api/jobs/internal/" + jobId;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authorizationHeader);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<JobSummaryResponse> response =
                    restTemplate.exchange(url, HttpMethod.GET, entity, JobSummaryResponse.class);

            return response.getBody();
        } catch (HttpClientErrorException.NotFound ex) {
            throw new ResourceNotFoundException("Job not found with id: " + jobId);
        } catch (HttpClientErrorException ex) {
            throw new BadRequestException("Failed to validate job with job-service");
        }
    }
}