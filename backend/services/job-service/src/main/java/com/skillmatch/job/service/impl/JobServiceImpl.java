package com.skillmatch.job.service.impl;

import com.skillmatch.job.dto.request.CreateJobRequest;
import com.skillmatch.job.dto.request.UpdateJobRequest;
import com.skillmatch.job.dto.response.InternalJobSummaryResponse;
import com.skillmatch.job.dto.response.JobResponse;
import com.skillmatch.job.dto.response.JobSummaryResponse;
import com.skillmatch.job.entity.Job;
import com.skillmatch.job.entity.JobStatus;
import com.skillmatch.job.entity.EmploymentType;
import com.skillmatch.job.entity.ExperienceLevel;
import com.skillmatch.job.exception.ForbiddenException;
import com.skillmatch.job.exception.ResourceNotFoundException;
import com.skillmatch.job.repository.JobRepository;
import com.skillmatch.job.repository.SavedJobRepository;
import com.skillmatch.job.security.SecurityUtils;
import com.skillmatch.job.service.JobService;
import com.skillmatch.job.specification.JobSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class JobServiceImpl implements JobService {

    private static final String ROLE_RECRUITER = "RECRUITER";

    private final JobRepository jobRepository;
    private final SavedJobRepository savedJobRepository;

    @Override
    public JobResponse createJob(CreateJobRequest request) {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        String currentUserRole = SecurityUtils.getCurrentUserRole();

        validateRecruiterRole(currentUserRole);
        validateSalaryRange(request.getSalaryMin(), request.getSalaryMax());

        Job job = Job.builder()
                .recruiterId(currentUserId)
                .title(request.getTitle().trim())
                .description(request.getDescription().trim())
                .location(request.getLocation().trim())
                .employmentType(request.getEmploymentType())
                .experienceLevel(request.getExperienceLevel())
                .requiredSkills(request.getRequiredSkills())
                .salaryMin(request.getSalaryMin())
                .salaryMax(request.getSalaryMax())
                .applicationDeadline(request.getApplicationDeadline())
                .status(JobStatus.DRAFT)
                .build();

        Job savedJob = jobRepository.save(job);
        return mapToJobResponse(savedJob, false);
    }

    @Override
    @CacheEvict(value = "internalJobSummary", key = "#jobId")
    public JobResponse updateJob(Long jobId, UpdateJobRequest request) {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        String currentUserRole = SecurityUtils.getCurrentUserRole();

        validateRecruiterRole(currentUserRole);
        validateSalaryRange(request.getSalaryMin(), request.getSalaryMax());

        Job job = getOwnedJobOrThrow(jobId, currentUserId);

        job.setTitle(request.getTitle().trim());
        job.setDescription(request.getDescription().trim());
        job.setLocation(request.getLocation().trim());
        job.setEmploymentType(request.getEmploymentType());
        job.setExperienceLevel(request.getExperienceLevel());
        job.setRequiredSkills(request.getRequiredSkills());
        job.setSalaryMin(request.getSalaryMin());
        job.setSalaryMax(request.getSalaryMax());
        job.setApplicationDeadline(request.getApplicationDeadline());

        Job updatedJob = jobRepository.save(job);
        boolean saved = savedJobRepository.existsByUserIdAndJob_Id(currentUserId, updatedJob.getId());

        return mapToJobResponse(updatedJob, saved);
    }

    @Override
    @CacheEvict(value = "internalJobSummary", key = "#jobId")
    public void deleteJob(Long jobId) {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        String currentUserRole = SecurityUtils.getCurrentUserRole();

        validateRecruiterRole(currentUserRole);

        Job job = getOwnedJobOrThrow(jobId, currentUserId);
        jobRepository.delete(job);
    }

    @Override
    @CacheEvict(value = "internalJobSummary", key = "#jobId")
    public JobResponse publishJob(Long jobId) {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        String currentUserRole = SecurityUtils.getCurrentUserRole();

        validateRecruiterRole(currentUserRole);

        Job job = getOwnedJobOrThrow(jobId, currentUserId);
        job.setStatus(JobStatus.PUBLISHED);

        Job updatedJob = jobRepository.save(job);
        boolean saved = savedJobRepository.existsByUserIdAndJob_Id(currentUserId, updatedJob.getId());

        return mapToJobResponse(updatedJob, saved);
    }

    @Override
    @CacheEvict(value = "internalJobSummary", key = "#jobId")
    public JobResponse unpublishJob(Long jobId) {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        String currentUserRole = SecurityUtils.getCurrentUserRole();

        validateRecruiterRole(currentUserRole);

        Job job = getOwnedJobOrThrow(jobId, currentUserId);
        job.setStatus(JobStatus.DRAFT);

        Job updatedJob = jobRepository.save(job);
        boolean saved = savedJobRepository.existsByUserIdAndJob_Id(currentUserId, updatedJob.getId());

        return mapToJobResponse(updatedJob, saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<JobSummaryResponse> getPublishedJobs(
            String keyword,
            String location,
            EmploymentType employmentType,
            ExperienceLevel experienceLevel,
            Pageable pageable
    ) {
        Long currentUserId = SecurityUtils.getCurrentUserId();

        Specification<Job> spec = Specification
                .where(JobSpecification.isPublished())
                .and(JobSpecification.keywordContains(keyword))
                .and(JobSpecification.locationEquals(location))
                .and(JobSpecification.employmentTypeEquals(employmentType))
                .and(JobSpecification.experienceLevelEquals(experienceLevel));

        Page<Job> jobs = jobRepository.findAll(spec, pageable);

        return jobs.map(job -> {
            boolean saved = savedJobRepository.existsByUserIdAndJob_Id(currentUserId, job.getId());
            return mapToJobSummaryResponse(job, saved);
        });
    }

    @Override
    @Transactional(readOnly = true)
    public JobResponse getPublishedJobById(Long jobId) {
        Long currentUserId = SecurityUtils.getCurrentUserId();

        Job job = jobRepository.findByIdAndStatus(jobId, JobStatus.PUBLISHED)
                .orElseThrow(() -> new ResourceNotFoundException("Published job not found with id: " + jobId));

        boolean saved = savedJobRepository.existsByUserIdAndJob_Id(currentUserId, job.getId());
        return mapToJobResponse(job, saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<JobSummaryResponse> getMyJobs(Pageable pageable) {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        String currentUserRole = SecurityUtils.getCurrentUserRole();

        validateRecruiterRole(currentUserRole);

        Page<Job> jobs = jobRepository.findByRecruiterId(currentUserId, pageable);

        return jobs.map(job -> {
            boolean saved = savedJobRepository.existsByUserIdAndJob_Id(currentUserId, job.getId());
            return mapToJobSummaryResponse(job, saved);
        });
    }

    @Override
    @Transactional(readOnly = true)
    public JobResponse getMyJobById(Long jobId) {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        String currentUserRole = SecurityUtils.getCurrentUserRole();

        validateRecruiterRole(currentUserRole);

        Job job = getOwnedJobOrThrow(jobId, currentUserId);
        boolean saved = savedJobRepository.existsByUserIdAndJob_Id(currentUserId, job.getId());

        return mapToJobResponse(job, saved);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "internalJobSummary", key = "#jobId")
    public InternalJobSummaryResponse getInternalJobSummary(Long jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + jobId));

        return InternalJobSummaryResponse.builder()
                .id(job.getId())
                .recruiterId(job.getRecruiterId())
                .status(job.getStatus().name())
                .build();
    }

    private void validateRecruiterRole(String role) {
        if (!ROLE_RECRUITER.equals(role)) {
            throw new ForbiddenException("Only recruiters can perform this action");
        }
    }

    private Job getOwnedJobOrThrow(Long jobId, Long recruiterId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + jobId));

        if (!job.getRecruiterId().equals(recruiterId)) {
            throw new ForbiddenException("You are not allowed to modify this job");
        }

        return job;
    }

    private void validateSalaryRange(Integer salaryMin, Integer salaryMax) {
        if (salaryMin != null && salaryMin <= 0) {
            throw new IllegalArgumentException("salaryMin must be greater than 0");
        }

        if (salaryMax != null && salaryMax <= 0) {
            throw new IllegalArgumentException("salaryMax must be greater than 0");
        }

        if (salaryMin != null && salaryMax != null && salaryMin > salaryMax) {
            throw new IllegalArgumentException("salaryMin cannot be greater than salaryMax");
        }
    }

    private JobResponse mapToJobResponse(Job job, boolean saved) {
        return JobResponse.builder()
                .id(job.getId())
                .recruiterId(job.getRecruiterId())
                .title(job.getTitle())
                .description(job.getDescription())
                .location(job.getLocation())
                .employmentType(job.getEmploymentType())
                .experienceLevel(job.getExperienceLevel())
                .requiredSkills(job.getRequiredSkills())
                .salaryMin(job.getSalaryMin())
                .salaryMax(job.getSalaryMax())
                .applicationDeadline(job.getApplicationDeadline())
                .status(job.getStatus())
                .saved(saved)
                .createdAt(job.getCreatedAt())
                .updatedAt(job.getUpdatedAt())
                .build();
    }

    private JobSummaryResponse mapToJobSummaryResponse(Job job, boolean saved) {
        return JobSummaryResponse.builder()
                .id(job.getId())
                .title(job.getTitle())
                .location(job.getLocation())
                .employmentType(job.getEmploymentType())
                .experienceLevel(job.getExperienceLevel())
                .salaryMin(job.getSalaryMin())
                .salaryMax(job.getSalaryMax())
                .applicationDeadline(job.getApplicationDeadline())
                .saved(saved)
                .createdAt(job.getCreatedAt())
                .build();
    }
}