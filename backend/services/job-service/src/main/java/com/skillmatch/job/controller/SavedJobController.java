package com.skillmatch.job.controller;

import com.skillmatch.job.dto.response.JobSummaryResponse;
import com.skillmatch.job.service.SavedJobService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
@Tag(name = "Saved Job API", description = "APIs for saving and unsaving jobs")
public class SavedJobController {

    private final SavedJobService savedJobService;

    @PostMapping("/{jobId}/save")
    @Operation(summary = "Save a published job")
    public ResponseEntity<Void> saveJob(@PathVariable Long jobId) {
        savedJobService.saveJob(jobId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{jobId}/save")
    @Operation(summary = "Unsave a saved job")
    public ResponseEntity<Void> unsaveJob(@PathVariable Long jobId) {
        savedJobService.unsaveJob(jobId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/saved")
    @Operation(summary = "Get my saved jobs")
    public ResponseEntity<Page<JobSummaryResponse>> getMySavedJobs(
            @ParameterObject
            @PageableDefault(size = 10, sort = "savedAt", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        Page<JobSummaryResponse> response = savedJobService.getMySavedJobs(pageable);
        return ResponseEntity.ok(response);
    }
}