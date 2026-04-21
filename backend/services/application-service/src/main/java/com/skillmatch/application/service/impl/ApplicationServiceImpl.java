package com.skillmatch.application.service.impl;

import com.skillmatch.application.dto.request.ApplyRequest;
import com.skillmatch.application.dto.request.UpdateApplicationStatusRequest;
import com.skillmatch.application.dto.response.ApplicationResponse;
import com.skillmatch.application.dto.response.JobSummaryResponse;
import com.skillmatch.application.entity.Application;
import com.skillmatch.application.entity.ApplicationStatus;
import com.skillmatch.application.entity.ApplicationStatusHistory;
import com.skillmatch.application.exception.BadRequestException;
import com.skillmatch.application.exception.ForbiddenException;
import com.skillmatch.application.exception.ResourceNotFoundException;
import com.skillmatch.application.mapper.ApplicationMapper;
import com.skillmatch.application.repository.ApplicationRepository;
import com.skillmatch.application.repository.ApplicationStatusHistoryRepository;
import com.skillmatch.application.security.SecurityUtils;
import com.skillmatch.application.service.ApplicationService;
import com.skillmatch.application.service.JobClientService;
import com.skillmatch.application.util.RoleConstants;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final ApplicationStatusHistoryRepository historyRepository;
    private final ApplicationMapper applicationMapper;
    private final JobClientService jobClientService;

    @Override
    public ApplicationResponse apply(ApplyRequest request) {
        validateRole(RoleConstants.JOB_SEEKER);

        Long currentUserId = SecurityUtils.getCurrentUserId();
        String authorizationHeader = "Bearer " + SecurityUtils.getCurrentToken();

        if (applicationRepository.existsByJobIdAndApplicantId(request.getJobId(), currentUserId)) {
            throw new BadRequestException("You have already applied to this job");
        }

        JobSummaryResponse job = jobClientService.getJobById(request.getJobId(), authorizationHeader);

        if (job == null) {
            throw new ResourceNotFoundException("Job not found with id: " + request.getJobId());
        }

        if (!"PUBLISHED".equalsIgnoreCase(job.getStatus())) {
            throw new BadRequestException("You can only apply to published jobs");
        }

        LocalDateTime now = LocalDateTime.now();

        Application application = Application.builder()
                .jobId(request.getJobId())
                .applicantId(currentUserId)
                .status(ApplicationStatus.APPLIED)
                .appliedAt(now)
                .updatedAt(now)
                .build();

        Application saved = applicationRepository.save(application);

        ApplicationStatusHistory history = ApplicationStatusHistory.builder()
                .application(saved)
                .status(ApplicationStatus.APPLIED)
                .changedAt(now)
                .changedBy(currentUserId)
                .build();

        historyRepository.save(history);
        saved.getStatusHistory().add(history);

        return applicationMapper.toResponse(saved);
    }

    @Override
    public ApplicationResponse withdraw(Long applicationId) {
        validateRole(RoleConstants.JOB_SEEKER);

        Long currentUserId = SecurityUtils.getCurrentUserId();

        Application application = applicationRepository.findWithStatusHistoryById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found with id: " + applicationId));

        if (!application.getApplicantId().equals(currentUserId)) {
            throw new ForbiddenException("You can only withdraw your own application");
        }

        if (application.getStatus() == ApplicationStatus.WITHDRAWN) {
            throw new BadRequestException("Application is already withdrawn");
        }

        LocalDateTime now = LocalDateTime.now();
        application.setStatus(ApplicationStatus.WITHDRAWN);
        application.setWithdrawnAt(now);
        application.setUpdatedAt(now);

        ApplicationStatusHistory history = ApplicationStatusHistory.builder()
                .application(application)
                .status(ApplicationStatus.WITHDRAWN)
                .changedAt(now)
                .changedBy(currentUserId)
                .build();

        historyRepository.save(history);
        application.getStatusHistory().add(history);

        return applicationMapper.toResponse(applicationRepository.save(application));
    }

    @Override
    public List<ApplicationResponse> getMyApplications() {
        validateRole(RoleConstants.JOB_SEEKER);

        Long currentUserId = SecurityUtils.getCurrentUserId();

        return applicationRepository.findByApplicantIdOrderByAppliedAtDesc(currentUserId)
                .stream()
                .map(applicationMapper::toResponse)
                .toList();
    }

    @Override
    public Page<ApplicationResponse> getApplicationsByJob(Long jobId, Pageable pageable) {
        validateRole(RoleConstants.RECRUITER);

        Long recruiterId = SecurityUtils.getCurrentUserId();
        String authorizationHeader = "Bearer " + SecurityUtils.getCurrentToken();

        JobSummaryResponse job = jobClientService.getJobById(jobId, authorizationHeader);

        if (job == null) {
            throw new ResourceNotFoundException("Job not found with id: " + jobId);
        }

        if (!recruiterId.equals(job.getRecruiterId())) {
            throw new ForbiddenException("You can only view applications for your own jobs");
        }

        return applicationRepository.findByJobId(jobId, pageable)
                .map(applicationMapper::toResponse);
    }

    @Override
    public ApplicationResponse updateApplicationStatus(Long applicationId,
                                                       UpdateApplicationStatusRequest request) {
        validateRole(RoleConstants.RECRUITER);

        Long recruiterId = SecurityUtils.getCurrentUserId();
        String authorizationHeader = "Bearer " + SecurityUtils.getCurrentToken();

        Application application = applicationRepository.findWithStatusHistoryById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found with id: " + applicationId));

        JobSummaryResponse job = jobClientService.getJobById(application.getJobId(), authorizationHeader);

        if (job == null) {
            throw new ResourceNotFoundException("Job not found with id: " + application.getJobId());
        }

        if (!recruiterId.equals(job.getRecruiterId())) {
            throw new ForbiddenException("You can only update applications for your own jobs");
        }

        if (application.getStatus() == ApplicationStatus.WITHDRAWN) {
            throw new BadRequestException("Withdrawn applications cannot be updated");
        }

        if (request.getStatus() == ApplicationStatus.WITHDRAWN) {
            throw new BadRequestException("Recruiter cannot set status to WITHDRAWN");
        }

        if (application.getStatus() == request.getStatus()) {
            throw new BadRequestException("Application already has status: " + request.getStatus());
        }

        LocalDateTime now = LocalDateTime.now();
        application.setStatus(request.getStatus());
        application.setUpdatedAt(now);

        ApplicationStatusHistory history = ApplicationStatusHistory.builder()
                .application(application)
                .status(request.getStatus())
                .changedAt(now)
                .changedBy(recruiterId)
                .build();

        historyRepository.save(history);
        application.getStatusHistory().add(history);

        return applicationMapper.toResponse(applicationRepository.save(application));
    }

    private void validateRole(String expectedRole) {
        String currentRole = SecurityUtils.getCurrentUserRole();
        if (!expectedRole.equals(currentRole)) {
            throw new ForbiddenException("Access denied");
        }
    }
}