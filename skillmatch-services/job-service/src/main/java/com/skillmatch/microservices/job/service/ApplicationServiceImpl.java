package com.skillmatch.microservices.job.service;

import com.skillmatch.microservices.job.dto.ApplicationResponse;
import com.skillmatch.microservices.job.dto.JobResponse;
import com.skillmatch.microservices.job.dto.ResumeDTO;
import com.skillmatch.microservices.job.exception.JobNotFoundException;
import com.skillmatch.microservices.job.mapper.ApplicationMapper;
import com.skillmatch.microservices.job.mapper.JobMapper;
import com.skillmatch.microservices.job.model.Job;
import com.skillmatch.microservices.job.model.JobApplication;
import com.skillmatch.microservices.job.repository.JobApplicationRepository;
import com.skillmatch.microservices.job.repository.JobRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {

    private final JobRepository jobRepository;
    private final JobApplicationRepository applicationRepository;
    private final ResumeClient resumeClient;
    private final HttpServletRequest request;
    private final ApplicationMapper applicationMapper;
    private final JobMapper jobMapper;

    @Override
    public JobApplication applyToJob(Long jobId, Long userId) {

        Job job = getJobOrThrow(jobId);
        ResumeDTO resume = fetchResume(userId);

        validateResume(resume);

        double matchScore = calculateMatchScore(job.getSkills(), resume.skills());

        JobApplication application = buildApplication(jobId, userId, resume.id(), matchScore);

        return applicationRepository.save(application);
    }


    @Override
    public List<JobResponse> getJobsAppliedByUser(Long userId) {

        List<Long> jobIds = applicationRepository.findByUserId(userId)
                .stream()
                .map(JobApplication::getJobId)
                .toList();

        // Fetch jobs
        List<Job> jobs = jobRepository.findAllById(jobIds);

        // Convert to JobResponse DTO
        return jobs.stream()
                .map(jobMapper::toResponse)
                .toList();
    }


    /* ============================================================
       PRIVATE HELPER METHODS (Single Responsibility Principle)
       ============================================================ */

    private Job getJobOrThrow(Long jobId) {
        return jobRepository.findById(jobId)
                .orElseThrow(() -> new JobNotFoundException(jobId));
    }

    private ResumeDTO fetchResume(Long userId) {
        String token = request.getHeader("Authorization");
        return resumeClient.getResumeByUserId(token, userId);
    }

    private void validateResume(ResumeDTO resume) {
        if (resume == null) {
            throw new RuntimeException("Resume not found for user.");
        }

        if (resume.skills() == null || resume.skills().isEmpty()) {
            throw new RuntimeException("Resume contains no skills.");
        }
    }

    private double calculateMatchScore(List<String> jobSkills, List<String> resumeSkills) {

        if (jobSkills == null || jobSkills.isEmpty()) {
            return 0.0;
        }

        long matches = resumeSkills.stream()
                .filter(jobSkills::contains)
                .count();

        return (double) matches / jobSkills.size() * 100.0;
    }

    private JobApplication buildApplication(Long jobId, Long userId, Long resumeId, double score) {

        JobApplication app = new JobApplication();
        app.setJobId(jobId);
        app.setUserId(userId);
        app.setResumeId(resumeId);
        app.setMatchScore(score);

        return app;
    }

    @Override
    public List<ApplicationResponse> getApplicantsForJob(Long jobId) {

        List<JobApplication> apps = applicationRepository.findByJobId(jobId);

        return apps.stream()
                .map(app -> {
                    String token = request.getHeader("Authorization");
                    String name = resumeClient.getUserName(token, app.getUserId());
                    return applicationMapper.toResponse(app, name);
                })
                .toList();
    }

    @Override
    public ApplicationResponse updateStatus(Long applicationId, String status) {

        JobApplication app = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        app.setStatus(status);

        JobApplication saved = applicationRepository.save(app);

        String token = request.getHeader("Authorization");
        String userName = resumeClient.getUserName(token, saved.getUserId());

        return applicationMapper.toResponse(saved, userName);
    }

}
