package com.skillmatch.job.service;

import com.skillmatch.job.dto.request.CreateJobRequest;
import com.skillmatch.job.dto.request.UpdateJobRequest;
import com.skillmatch.job.dto.response.InternalJobSummaryResponse;
import com.skillmatch.job.dto.response.JobResponse;
import com.skillmatch.job.dto.response.JobSummaryResponse;
import com.skillmatch.job.entity.EmploymentType;
import com.skillmatch.job.entity.ExperienceLevel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface JobService {

    JobResponse createJob(CreateJobRequest request);

    JobResponse updateJob(Long jobId, UpdateJobRequest request);

    void deleteJob(Long jobId);

    JobResponse publishJob(Long jobId);

    JobResponse unpublishJob(Long jobId);

    Page<JobSummaryResponse> getPublishedJobs(
            String keyword,
            String location,
            EmploymentType employmentType,
            ExperienceLevel experienceLevel,
            Pageable pageable
    );

    JobResponse getPublishedJobById(Long jobId);

    Page<JobSummaryResponse> getMyJobs(Pageable pageable);

    JobResponse getMyJobById(Long jobId);
    InternalJobSummaryResponse getInternalJobSummary(Long jobId);
    List<InternalJobSummaryResponse> getInternalJobsBatch(List<Long> jobIds);
}