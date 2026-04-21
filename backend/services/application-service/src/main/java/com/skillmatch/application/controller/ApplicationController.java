package com.skillmatch.application.controller;

import com.skillmatch.application.dto.request.ApplyRequest;
import com.skillmatch.application.dto.request.UpdateApplicationStatusRequest;
import com.skillmatch.application.dto.response.ApplicationResponse;
import com.skillmatch.application.service.ApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
@Tag(name = "Application API", description = "APIs for managing job applications between job seekers and recruiters")
public class ApplicationController {

    private final ApplicationService applicationService;

    @PostMapping
    @Operation(
            summary = "Apply to a job",
            description = "ROLE: JOB_SEEKER\nAllows a job seeker to apply to a published job. Prevents duplicate applications."
    )
    public ResponseEntity<ApplicationResponse> apply(@Valid @RequestBody ApplyRequest request) {
        return ResponseEntity.ok(applicationService.apply(request));
    }

    @PatchMapping("/{applicationId}/withdraw")
    @Operation(
            summary = "Withdraw application",
            description = "ROLE: JOB_SEEKER\nAllows a job seeker to withdraw their own application. Status becomes WITHDRAWN."
    )
    public ResponseEntity<ApplicationResponse> withdraw(@PathVariable Long applicationId) {
        return ResponseEntity.ok(applicationService.withdraw(applicationId));
    }

    @GetMapping("/me")
    @Operation(
            summary = "Get my applications",
            description = "ROLE: JOB_SEEKER\nReturns all applications created by the authenticated user."
    )
    public ResponseEntity<List<ApplicationResponse>> getMyApplications() {
        return ResponseEntity.ok(applicationService.getMyApplications());
    }

    @GetMapping("/job/{jobId}")
    @Operation(
            summary = "Get applications for a job",
            description = "ROLE: RECRUITER\nReturns paginated applications for a job owned by the authenticated recruiter."
    )
    public ResponseEntity<Page<ApplicationResponse>> getApplicationsByJob(
            @PathVariable Long jobId,
            @ParameterObject
            @PageableDefault(size = 20, sort = "appliedAt", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        return ResponseEntity.ok(applicationService.getApplicationsByJob(jobId, pageable));
    }

    @PatchMapping("/{applicationId}/status")
    @Operation(
            summary = "Update application status",
            description = "ROLE: RECRUITER\nAllows a recruiter to update the status of an application for their job. Cannot set WITHDRAWN."
    )
    public ResponseEntity<ApplicationResponse> updateStatus(
            @PathVariable Long applicationId,
            @Valid @RequestBody UpdateApplicationStatusRequest request
    ) {
        return ResponseEntity.ok(applicationService.updateApplicationStatus(applicationId, request));
    }
}