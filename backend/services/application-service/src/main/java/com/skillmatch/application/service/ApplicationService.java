package com.skillmatch.application.service;

import com.skillmatch.application.dto.request.ApplyRequest;
import com.skillmatch.application.dto.request.UpdateApplicationStatusRequest;
import com.skillmatch.application.dto.response.ApplicationResponse;

import java.util.List;

public interface ApplicationService {
    ApplicationResponse apply(ApplyRequest request);
    ApplicationResponse withdraw(Long applicationId);
    List<ApplicationResponse> getMyApplications();
    List<ApplicationResponse> getApplicationsByJob(Long jobId);
    ApplicationResponse updateApplicationStatus(Long applicationId, UpdateApplicationStatusRequest request);
}