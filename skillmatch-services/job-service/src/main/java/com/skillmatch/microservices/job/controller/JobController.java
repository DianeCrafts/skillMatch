package com.skillmatch.microservices.job.controller;

import com.skillmatch.microservices.job.Dto.CreateJobRequest;
import com.skillmatch.microservices.job.Dto.JobResponse;
import com.skillmatch.microservices.job.Dto.UpdateJobRequest;
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

    /** CREATE A JOB */
    @PostMapping
    public ResponseEntity<JobResponse> createJob(
            @RequestHeader("X-User-Id") Long recruiterId,
            @RequestBody CreateJobRequest request) {

        Job job = jobService.createJob(recruiterId, request);

        JobResponse response = new JobResponse(
                job.getId(),
                job.getTitle(),
                job.getDescription(),
                job.getRequirements(),
                job.getLocation(),
                job.getSalary(),
                job.getExperience(),
                job.getSkills(),
                job.isRemote()
        );

        return ResponseEntity.ok(response);
    }

    /** APPLY TO A JOB */
    @PostMapping("/{jobId}/apply")
    public ResponseEntity<JobApplication> apply(
            @PathVariable Long jobId,
            @RequestHeader("X-User-Id") Long userId) {

        JobApplication app = applicationService.applyToJob(jobId, userId);
        return ResponseEntity.ok(app);
    }

    /** GET ALL JOBS */
    @GetMapping
    public ResponseEntity<List<JobResponse>> getAllJobs() {

        List<Job> jobs = jobService.getAllJobs();

        List<JobResponse> responses = jobs.stream()
                .map(job -> new JobResponse(
                        job.getId(),
                        job.getTitle(),
                        job.getDescription(),
                        job.getRequirements(),
                        job.getLocation(),
                        job.getSalary(),
                        job.getExperience(),
                        job.getSkills(),
                        job.isRemote()
                ))
                .toList();

        return ResponseEntity.ok(responses);
    }

    /** GET SINGLE JOB BY ID */
    @GetMapping("/{id}")
    public ResponseEntity<JobResponse> getJobById(@PathVariable Long id) {

        Job job = jobService.getJob(id);

        JobResponse response = new JobResponse(
                job.getId(),
                job.getTitle(),
                job.getDescription(),
                job.getRequirements(),
                job.getLocation(),
                job.getSalary(),
                job.getExperience(),
                job.getSkills(),
                job.isRemote()
        );

        return ResponseEntity.ok(response);
    }



    @GetMapping("/recruiter")
    public ResponseEntity<List<JobResponse>> getJobsByRecruiter(
            @RequestHeader("X-User-Id") Long recruiterId) {

        List<Job> jobs = jobService.getJobsByRecruiter(recruiterId);

        List<JobResponse> responses = jobs.stream()
                .map(job -> new JobResponse(
                        job.getId(),
                        job.getTitle(),
                        job.getDescription(),
                        job.getRequirements(),
                        job.getLocation(),
                        job.getSalary(),
                        job.getExperience(),
                        job.getSkills(),
                        job.isRemote()
                ))
                .toList();

        return ResponseEntity.ok(responses);
    }

    /** UPDATE A JOB */
    @PutMapping("/{id}")
    public ResponseEntity<JobResponse> updateJob(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long recruiterId,
            @RequestBody UpdateJobRequest request) {

        Job updated = jobService.updateJob(id, request);

        JobResponse response = new JobResponse(
                updated.getId(),
                updated.getTitle(),
                updated.getDescription(),
                updated.getRequirements(),
                updated.getLocation(),
                updated.getSalary(),
                updated.getExperience(),
                updated.getSkills(),
                updated.isRemote()
        );

        return ResponseEntity.ok(response);
    }

    /** DELETE A JOB */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJob(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long recruiterId) {

        jobService.deleteJob(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/applied")
    public ResponseEntity<List<JobResponse>> getAppliedJobs(
            @RequestHeader("X-User-Id") Long userId) {

        List<Job> jobs = applicationService.getJobsAppliedByUser(userId);

        List<JobResponse> responses = jobs.stream()
                .map(job -> new JobResponse(
                        job.getId(),
                        job.getTitle(),
                        job.getDescription(),
                        job.getRequirements(),
                        job.getLocation(),
                        job.getSalary(),
                        job.getExperience(),
                        job.getSkills(),
                        job.isRemote()
                ))
                .toList();

        return ResponseEntity.ok(responses);
    }


}
