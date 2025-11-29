package com.skillmatch.microservices.job.service;

import com.skillmatch.microservices.job.dto.ApplicationResponse;
import com.skillmatch.microservices.job.dto.JobResponse;
import com.skillmatch.microservices.job.model.Job;
import com.skillmatch.microservices.job.model.JobApplication;

import java.util.List;

public interface ApplicationService {
    JobApplication applyToJob(Long jobId, Long userId);
    List<JobResponse> getJobsAppliedByUser(Long userId);
    List<ApplicationResponse> getApplicantsForJob(Long jobId);

    ApplicationResponse updateStatus(Long applicationId, String status);

}
