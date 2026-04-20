package com.skillmatch.job.service.impl;

import com.skillmatch.job.dto.response.JobSummaryResponse;
import com.skillmatch.job.entity.Job;
import com.skillmatch.job.entity.JobStatus;
import com.skillmatch.job.entity.SavedJob;
import com.skillmatch.job.exception.DuplicateResourceException;
import com.skillmatch.job.exception.ResourceNotFoundException;
import com.skillmatch.job.repository.JobRepository;
import com.skillmatch.job.repository.SavedJobRepository;
import com.skillmatch.job.security.SecurityUtils;
import com.skillmatch.job.service.SavedJobService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class SavedJobServiceImpl implements SavedJobService {

    private final SavedJobRepository savedJobRepository;
    private final JobRepository jobRepository;

    @Override
    public void saveJob(Long jobId) {
        Long currentUserId = SecurityUtils.getCurrentUserId();

        Job job = jobRepository.findByIdAndStatus(jobId, JobStatus.PUBLISHED)
                .orElseThrow(() -> new ResourceNotFoundException("Published job not found with id: " + jobId));

        boolean alreadySaved = savedJobRepository.existsByUserIdAndJob_Id(currentUserId, jobId);
        if (alreadySaved) {
            throw new DuplicateResourceException("Job is already saved");
        }

        SavedJob savedJob = SavedJob.builder()
                .userId(currentUserId)
                .job(job)
                .build();

        savedJobRepository.save(savedJob);
    }

    @Override
    public void unsaveJob(Long jobId) {
        Long currentUserId = SecurityUtils.getCurrentUserId();

        SavedJob savedJob = savedJobRepository.findByUserIdAndJob_Id(currentUserId, jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Saved job not found for current user"));

        savedJobRepository.delete(savedJob);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<JobSummaryResponse> getMySavedJobs(Pageable pageable) {
        Long currentUserId = SecurityUtils.getCurrentUserId();

        Page<SavedJob> savedJobs = savedJobRepository.findByUserId(currentUserId, pageable);

        return savedJobs.map(savedJob -> {
            Job job = savedJob.getJob();

            return JobSummaryResponse.builder()
                    .id(job.getId())
                    .title(job.getTitle())
                    .location(job.getLocation())
                    .employmentType(job.getEmploymentType())
                    .experienceLevel(job.getExperienceLevel())
                    .salaryMin(job.getSalaryMin())
                    .salaryMax(job.getSalaryMax())
                    .applicationDeadline(job.getApplicationDeadline())
                    .saved(true)
                    .createdAt(job.getCreatedAt())
                    .build();
        });
    }
}