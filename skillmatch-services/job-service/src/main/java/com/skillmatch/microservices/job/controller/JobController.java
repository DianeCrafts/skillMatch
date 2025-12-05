package com.skillmatch.microservices.job.controller;

import com.skillmatch.microservices.job.dto.CreateJobRequest;
import com.skillmatch.microservices.job.dto.JobResponse;
import com.skillmatch.microservices.job.dto.UpdateJobRequest;
import com.skillmatch.microservices.job.mapper.JobMapper;
import com.skillmatch.microservices.job.service.JobSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.skillmatch.microservices.job.service.JobService;
import com.skillmatch.microservices.job.model.Job;

import java.util.List;
@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;
    private final JobMapper jobMapper;
    private final JobSearchService jobSearchService;

    @PostMapping
    public ResponseEntity<JobResponse> createJob(
            @RequestHeader("X-User-Id") Long recruiterId,
            @RequestBody CreateJobRequest request) {
        Job job = jobService.createJob(recruiterId, request);
        return ResponseEntity.ok(jobMapper.toResponse(job));
    }

    @GetMapping
    public ResponseEntity<List<JobResponse>> getAllJobs() {
        return ResponseEntity.ok(
                jobService.getAllJobs()
                        .stream()
                        .map(jobMapper::toResponse)
                        .toList()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobResponse> getJobById(@PathVariable Long id) {
        return ResponseEntity.ok(
                jobMapper.toResponse(jobService.getJob(id))
        );
    }

    @GetMapping("/recruiter")
    public ResponseEntity<List<JobResponse>> getJobsByRecruiter(
            @RequestHeader("X-User-Id") Long recruiterId) {
        return ResponseEntity.ok(
                jobService.getJobsByRecruiter(recruiterId)
                        .stream()
                        .map(jobMapper::toResponse)
                        .toList()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<JobResponse> updateJob(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long recruiterId,
            @RequestBody UpdateJobRequest request) {
        Job updated = jobService.updateJob(id, request);
        return ResponseEntity.ok(jobMapper.toResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJob(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long recruiterId) {

        jobService.deleteJob(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<JobResponse>> searchJobs(
            @RequestParam String keyword
    ) {
        List<Job> results = jobSearchService.searchJobs(keyword);

        return ResponseEntity.ok(
                results.stream()
                        .map(jobMapper::toResponse)
                        .toList()
        );
    }

    @GetMapping("/recommended")
    public ResponseEntity<List<JobResponse>> getRecommendedJobs(
            @RequestHeader("X-User-Id") Long userId
    ) {
        List<Job> recommended = jobService.recommendJobs(userId);

        return ResponseEntity.ok(
                recommended.stream()
                        .map(jobMapper::toResponse)
                        .toList()
        );
    }

}