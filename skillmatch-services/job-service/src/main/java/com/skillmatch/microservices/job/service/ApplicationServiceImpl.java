package com.skillmatch.microservices.job.service;

import com.skillmatch.microservices.job.Dto.ResumeData;
import com.skillmatch.microservices.job.Dto.SkillData;
import com.skillmatch.microservices.job.exception.JobNotFoundException;
import com.skillmatch.microservices.job.model.Job;
import com.skillmatch.microservices.job.model.JobApplication;
import com.skillmatch.microservices.job.repository.JobApplicationRepository;
import com.skillmatch.microservices.job.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {

    private final JobRepository jobRepository;
    private final JobApplicationRepository applicationRepository;
    private final ResumeClient resumeClient;

    @Override
    public JobApplication applyToJob(String jobId, String userId) {

        // Validate job exists
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new JobNotFoundException(jobId));

        // Fetch parsed resume from Resume Service
        ResumeData resume = resumeClient.getResumeByUserId(userId);

        if (resume == null || resume.getSkills() == null) {
            throw new RuntimeException("User has no parsed resume uploaded");
        }

        // Count skill matches
        int matches = 0;
        for (SkillData skill : resume.getSkills()) {
            if (job.getSkillsRequired().contains(skill.getName())) {
                matches++;
            }
        }

        double score = job.getSkillsRequired().isEmpty()
                ? 0.0
                : (double) matches / job.getSkillsRequired().size() * 100.0;

        // Save job application
        JobApplication app = new JobApplication();
        app.setJobId(jobId);
        app.setUserId(userId);
        app.setResumeId(String.valueOf(resume.getId()));
        app.setMatchScore(score);

        return applicationRepository.save(app);
    }
}
