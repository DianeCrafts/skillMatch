package com.skillmatch.job.service;

import com.skillmatch.job.dto.response.JobSummaryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SavedJobService {

    void saveJob(Long jobId);

    void unsaveJob(Long jobId);

    Page<JobSummaryResponse> getMySavedJobs(Pageable pageable);
}