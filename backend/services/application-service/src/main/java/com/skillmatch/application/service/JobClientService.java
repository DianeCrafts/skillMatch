package com.skillmatch.application.service;

import com.skillmatch.application.dto.response.JobSummaryResponse;

public interface JobClientService {
    JobSummaryResponse getJobById(Long jobId, String authorizationHeader);
}