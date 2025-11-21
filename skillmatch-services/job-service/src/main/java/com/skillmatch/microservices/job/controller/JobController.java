package com.skillmatch.microservices.job.controller;

import com.skillmatch.microservices.job.Dto.CreateJobRequest;
import com.skillmatch.microservices.job.Dto.JobResponse;
import com.skillmatch.microservices.job.model.JobApplication;
import com.skillmatch.microservices.job.service.ApplicationService;
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
    private final ApplicationService applicationService;

    @PostMapping
    public ResponseEntity<JobResponse> createJob(
            @RequestHeader("X-User-Id") String recruiterId,
            @RequestBody CreateJobRequest request) {

        Job job = jobService.createJob(recruiterId, request);
        return ResponseEntity.ok(
                new JobResponse(
                        job.getId(),
                        job.getTitle(),
                        job.getDescription(),
                        job.getSkillsRequired(),
                        job.getLocation(),
                        job.isRemote()
                )
        );
    }

    @PostMapping("/{jobId}/apply")
    public ResponseEntity<JobApplication> apply(
            @PathVariable String jobId,
            @RequestHeader("X-User-Id") String userId) {

        return ResponseEntity.ok(applicationService.applyToJob(jobId, userId));
    }
}
