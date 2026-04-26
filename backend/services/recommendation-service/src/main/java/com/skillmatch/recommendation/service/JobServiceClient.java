package com.skillmatch.recommendation.service;

import com.skillmatch.recommendation.dto.response.JobDataResponse;
import com.skillmatch.recommendation.dto.response.ProfileDataResponse;
import com.skillmatch.recommendation.exception.ExternalServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JobServiceClient {

    private final RestClient jobRestClient;

    public List<JobDataResponse> getJobsByIds(List<Long> jobIds) {
        try {
            return jobRestClient.post()
                    .uri("/api/jobs/internal/batch")
                    .body(jobIds)
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {});
        } catch (Exception e) {
            throw new ExternalServiceException("Job Service call failed");
        }
    }

    public JobDataResponse getJobById(Long jobId) {
        try {
            return jobRestClient.get()
                    .uri("/api/jobs/internal/{jobId}", jobId)
                    .retrieve()
                    .body(JobDataResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ExternalServiceException("Job Service call failed");
        }
    }
}