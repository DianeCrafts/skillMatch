package com.skillmatch.microservices.job.service;

import com.skillmatch.microservices.job.model.Job;
import com.skillmatch.microservices.job.model.JobApplication;

import java.util.List;

public interface ApplicationService {
    JobApplication applyToJob(Long jobId, Long userId);
    List<Job> getJobsAppliedByUser(Long userId);

}
