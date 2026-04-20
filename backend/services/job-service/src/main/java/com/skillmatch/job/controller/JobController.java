package com.skillmatch.job.controller;

import com.skillmatch.job.dto.request.CreateJobRequest;
import com.skillmatch.job.dto.request.UpdateJobRequest;
import com.skillmatch.job.dto.response.JobResponse;
import com.skillmatch.job.dto.response.JobSummaryResponse;
import com.skillmatch.job.entity.EmploymentType;
import com.skillmatch.job.entity.ExperienceLevel;
import com.skillmatch.job.service.JobService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
@Tag(name = "Job API", description = "APIs for job creation, management, and browsing")
public class JobController {

    private final JobService jobService;

    @PostMapping
    @Operation(summary = "Create a new job")
    public ResponseEntity<JobResponse> createJob(@Valid @RequestBody CreateJobRequest request) {
        JobResponse response = jobService.createJob(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{jobId}")
    @Operation(summary = "Update an existing job")
    public ResponseEntity<JobResponse> updateJob(
            @PathVariable Long jobId,
            @Valid @RequestBody UpdateJobRequest request
    ) {
        JobResponse response = jobService.updateJob(jobId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{jobId}")
    @Operation(summary = "Delete a job")
    public ResponseEntity<Void> deleteJob(@PathVariable Long jobId) {
        jobService.deleteJob(jobId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{jobId}/publish")
    @Operation(summary = "Publish a job")
    public ResponseEntity<JobResponse> publishJob(@PathVariable Long jobId) {
        JobResponse response = jobService.publishJob(jobId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{jobId}/unpublish")
    @Operation(summary = "Unpublish a job")
    public ResponseEntity<JobResponse> unpublishJob(@PathVariable Long jobId) {
        JobResponse response = jobService.unpublishJob(jobId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Get published jobs with filters and pagination")
    public ResponseEntity<Page<JobSummaryResponse>> getPublishedJobs(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) EmploymentType employmentType,
            @RequestParam(required = false) ExperienceLevel experienceLevel,
            @ParameterObject
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        Page<JobSummaryResponse> response = jobService.getPublishedJobs(
                keyword,
                location,
                employmentType,
                experienceLevel,
                pageable
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{jobId}")
    @Operation(summary = "Get published job details by id")
    public ResponseEntity<JobResponse> getPublishedJobById(@PathVariable Long jobId) {
        JobResponse response = jobService.getPublishedJobById(jobId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    @Operation(summary = "Get my jobs (recruiter only)")
    public ResponseEntity<Page<JobSummaryResponse>> getMyJobs(
            @ParameterObject
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        Page<JobSummaryResponse> response = jobService.getMyJobs(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me/{jobId}")
    @Operation(summary = "Get my job details by id (recruiter only)")
    public ResponseEntity<JobResponse> getMyJobById(@PathVariable Long jobId) {
        JobResponse response = jobService.getMyJobById(jobId);
        return ResponseEntity.ok(response);
    }
}