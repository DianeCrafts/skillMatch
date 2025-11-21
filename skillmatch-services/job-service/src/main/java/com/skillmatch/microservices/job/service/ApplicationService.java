package com.skillmatch.microservices.job.service;

import com.skillmatch.microservices.job.model.JobApplication;

public interface ApplicationService {
    JobApplication applyToJob(String jobId, String userId);
}
