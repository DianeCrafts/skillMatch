package com.skillmatch.application.service;

import com.skillmatch.application.dto.request.ApplyRequest;
import com.skillmatch.application.dto.request.UpdateApplicationStatusRequest;
import com.skillmatch.application.dto.response.ApplicationResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ApplicationService {
    ApplicationResponse apply(ApplyRequest request);
    ApplicationResponse withdraw(Long applicationId);
    List<ApplicationResponse> getMyApplications();
    Page<ApplicationResponse> getApplicationsByJob(Long jobId, Pageable pageable);
    ApplicationResponse updateApplicationStatus(Long applicationId, UpdateApplicationStatusRequest request);
}