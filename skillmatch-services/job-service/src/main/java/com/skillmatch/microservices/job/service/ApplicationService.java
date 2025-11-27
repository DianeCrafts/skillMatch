package com.skillmatch.microservices.job.service;

import com.skillmatch.microservices.job.model.JobApplication;

public interface ApplicationService {
    JobApplication applyToJob(Long jobId, Long userId);
}
