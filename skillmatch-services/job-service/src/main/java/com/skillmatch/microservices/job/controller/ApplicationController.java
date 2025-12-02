package com.skillmatch.microservices.job.controller;
import com.skillmatch.microservices.job.dto.*;
import com.skillmatch.microservices.job.model.JobApplication;
import com.skillmatch.microservices.job.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;

    // APPLY
    @PostMapping("/{jobId}/apply")
    public ResponseEntity<JobApplication> applyToJob(
            @PathVariable Long jobId,
            @RequestHeader("X-User-Id") Long userId) {

        return ResponseEntity.ok(applicationService.applyToJob(jobId, userId));
    }

    // APPLIED JOBS FOR USER
    @GetMapping("/applied")
    public ResponseEntity<List<JobResponse>> getJobsAppliedByUser(
            @RequestHeader("X-User-Id") Long userId) {

        return ResponseEntity.ok(applicationService.getJobsAppliedByUser(userId));
    }

    // APPLICANTS FOR A JOB
    @GetMapping("/job/{jobId}")
    public ResponseEntity<List<ApplicationResponse>> getApplicantsForJob(
            @PathVariable Long jobId) {

        return ResponseEntity.ok(applicationService.getApplicantsForJob(jobId));
    }

    // UPDATE APPLICATION STATUS
    @PutMapping("/{applicationId}/status")
    public ResponseEntity<ApplicationResponse> updateStatus(
            @PathVariable Long applicationId,
            @RequestBody ApplicationStatusUpdateRequest req) {

        return ResponseEntity.ok(
                applicationService.updateStatus(applicationId, req.status())
        );
    }
}
