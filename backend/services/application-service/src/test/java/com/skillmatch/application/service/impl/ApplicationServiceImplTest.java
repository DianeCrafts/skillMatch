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
import com.skillmatch.application.security.CustomUserPrincipal;
import com.skillmatch.application.service.JobClientService;
import com.skillmatch.application.util.RoleConstants;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.data.domain.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class ApplicationServiceImplTest {

    @Mock
    private ApplicationRepository applicationRepository;

    @Mock
    private ApplicationStatusHistoryRepository historyRepository;

    @Mock
    private ApplicationMapper applicationMapper;

    @Mock
    private JobClientService jobClientService;

    @InjectMocks
    private ApplicationServiceImpl applicationService;

    @BeforeEach
    void setUp() {
        CustomUserPrincipal principal = new CustomUserPrincipal(2L, RoleConstants.JOB_SEEKER, "seeker@test.com");
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(principal, "mock-jwt-token", List.of());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void apply_shouldCreateApplication_whenJobIsPublishedAndNotDuplicate() {
        ApplyRequest request = new ApplyRequest();
        request.setJobId(1L);

        JobSummaryResponse job = new JobSummaryResponse();
        job.setId(1L);
        job.setRecruiterId(10L);
        job.setStatus("PUBLISHED");

        Application savedApplication = Application.builder()
                .id(100L)
                .jobId(1L)
                .applicantId(2L)
                .status(ApplicationStatus.APPLIED)
                .appliedAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        ApplicationResponse response = ApplicationResponse.builder()
                .id(100L)
                .jobId(1L)
                .applicantId(2L)
                .status(ApplicationStatus.APPLIED)
                .build();

        when(applicationRepository.existsByJobIdAndApplicantId(1L, 2L)).thenReturn(false);
        when(jobClientService.getJobById(eq(1L), anyString())).thenReturn(job);
        when(applicationRepository.save(any(Application.class))).thenReturn(savedApplication);
        when(applicationMapper.toResponse(savedApplication)).thenReturn(response);

        ApplicationResponse result = applicationService.apply(request);

        assertNotNull(result);
        assertEquals(100L, result.getId());

        verify(applicationRepository).save(any(Application.class));
        verify(historyRepository).save(any(ApplicationStatusHistory.class));
        verify(applicationMapper).toResponse(savedApplication);
    }

    @Test
    void apply_shouldThrow_whenDuplicateApplicationExists() {
        ApplyRequest request = new ApplyRequest();
        request.setJobId(1L);

        when(applicationRepository.existsByJobIdAndApplicantId(1L, 2L)).thenReturn(true);

        assertThrows(BadRequestException.class, () -> applicationService.apply(request));

        verify(applicationRepository, never()).save(any());
        verify(historyRepository, never()).save(any());
    }

    @Test
    void apply_shouldThrow_whenJobNotPublished() {
        ApplyRequest request = new ApplyRequest();
        request.setJobId(1L);

        JobSummaryResponse job = new JobSummaryResponse();
        job.setId(1L);
        job.setRecruiterId(10L);
        job.setStatus("DRAFT");

        when(applicationRepository.existsByJobIdAndApplicantId(1L, 2L)).thenReturn(false);
        when(jobClientService.getJobById(eq(1L), anyString())).thenReturn(job);

        assertThrows(BadRequestException.class, () -> applicationService.apply(request));
    }

    @Test
    void withdraw_shouldUpdateStatusToWithdrawn_whenOwnerWithdraws() {
        Application application = Application.builder()
                .id(1L)
                .jobId(1L)
                .applicantId(2L)
                .status(ApplicationStatus.APPLIED)
                .appliedAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        ApplicationResponse response = ApplicationResponse.builder()
                .id(1L)
                .status(ApplicationStatus.WITHDRAWN)
                .build();

        when(applicationRepository.findWithStatusHistoryById(1L)).thenReturn(Optional.of(application));
        when(applicationRepository.save(any(Application.class))).thenReturn(application);
        when(applicationMapper.toResponse(application)).thenReturn(response);

        ApplicationResponse result = applicationService.withdraw(1L);

        assertEquals(ApplicationStatus.WITHDRAWN, application.getStatus());
        assertNotNull(application.getWithdrawnAt());
        assertEquals(ApplicationStatus.WITHDRAWN, result.getStatus());

        verify(historyRepository).save(any(ApplicationStatusHistory.class));
    }

    @Test
    void withdraw_shouldThrow_whenUserDoesNotOwnApplication() {
        Application application = Application.builder()
                .id(1L)
                .applicantId(999L)
                .status(ApplicationStatus.APPLIED)
                .build();

        when(applicationRepository.findWithStatusHistoryById(1L)).thenReturn(Optional.of(application));

        assertThrows(ForbiddenException.class, () -> applicationService.withdraw(1L));
    }

    @Test
    void getApplicationsByJob_shouldReturnPagedResponses_forRecruiter() {
        CustomUserPrincipal recruiter = new CustomUserPrincipal(10L, RoleConstants.RECRUITER, "recruiter@test.com");
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(recruiter, "mock-jwt-token", List.of());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        JobSummaryResponse job = new JobSummaryResponse();
        job.setId(1L);
        job.setRecruiterId(10L);
        job.setStatus("PUBLISHED");

        Application application = Application.builder()
                .id(1L)
                .jobId(1L)
                .applicantId(2L)
                .status(ApplicationStatus.APPLIED)
                .build();

        ApplicationResponse response = ApplicationResponse.builder()
                .id(1L)
                .jobId(1L)
                .applicantId(2L)
                .status(ApplicationStatus.APPLIED)
                .build();

        Pageable pageable = PageRequest.of(0, 20, Sort.by(Sort.Direction.DESC, "appliedAt"));
        Page<Application> page = new PageImpl<>(List.of(application), pageable, 1);

        when(jobClientService.getJobById(eq(1L), anyString())).thenReturn(job);
        when(applicationRepository.findByJobId(1L, pageable)).thenReturn(page);
        when(applicationMapper.toResponse(application)).thenReturn(response);

        Page<ApplicationResponse> result = applicationService.getApplicationsByJob(1L, pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals(1L, result.getContent().get(0).getId());
    }

    @Test
    void updateApplicationStatus_shouldThrow_whenRecruiterTriesToSetWithdrawn() {
        CustomUserPrincipal recruiter = new CustomUserPrincipal(10L, RoleConstants.RECRUITER, "recruiter@test.com");
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(recruiter, "mock-jwt-token", List.of());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Application application = Application.builder()
                .id(1L)
                .jobId(1L)
                .applicantId(2L)
                .status(ApplicationStatus.APPLIED)
                .build();

        JobSummaryResponse job = new JobSummaryResponse();
        job.setId(1L);
        job.setRecruiterId(10L);
        job.setStatus("PUBLISHED");

        UpdateApplicationStatusRequest request = new UpdateApplicationStatusRequest();
        request.setStatus(ApplicationStatus.WITHDRAWN);

        when(applicationRepository.findWithStatusHistoryById(1L)).thenReturn(Optional.of(application));
        when(jobClientService.getJobById(eq(1L), anyString())).thenReturn(job);

        assertThrows(BadRequestException.class,
                () -> applicationService.updateApplicationStatus(1L, request));
    }

    @Test
    void withdraw_shouldThrow_whenApplicationNotFound() {
        when(applicationRepository.findWithStatusHistoryById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> applicationService.withdraw(999L));
    }
}