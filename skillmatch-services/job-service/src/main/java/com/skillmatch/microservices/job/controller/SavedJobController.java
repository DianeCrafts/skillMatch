package com.skillmatch.microservices.job.controller;
import com.skillmatch.microservices.job.dto.JobResponse;
import com.skillmatch.microservices.job.service.SavedJobService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.skillmatch.microservices.job.model.Job;

import java.util.List;

@RestController
@RequestMapping("/api/saved-jobs")
@RequiredArgsConstructor
public class SavedJobController {

    private final SavedJobService savedJobService;

    @PostMapping("/{jobId}/save")
    public ResponseEntity<String> saveJob(
            @PathVariable Long jobId,
            @RequestHeader("X-User-Id") Long userId) {

        savedJobService.saveJob(userId, jobId);
        return ResponseEntity.ok("Job saved");
    }

    @DeleteMapping("/{jobId}/unsave")
    public ResponseEntity<String> unsaveJob(
            @PathVariable Long jobId,
            @RequestHeader("X-User-Id") Long userId) {

        savedJobService.unsaveJob(userId, jobId);
        return ResponseEntity.ok("Job unsaved");
    }

    @GetMapping("/saved")
    public ResponseEntity<List<JobResponse>> getSavedJobs(
            @RequestHeader("X-User-Id") Long userId) {

        List<Job> jobs = savedJobService.getSavedJobs(userId);

        List<JobResponse> response = jobs.stream()
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

        return ResponseEntity.ok(response);
    }
}
