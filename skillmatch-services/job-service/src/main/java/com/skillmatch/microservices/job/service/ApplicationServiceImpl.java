package com.skillmatch.microservices.job.service;

import com.skillmatch.microservices.job.Dto.ResumeDTO;
import com.skillmatch.microservices.job.Dto.SkillData;
import com.skillmatch.microservices.job.exception.JobNotFoundException;
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
    @Override
    public JobApplication applyToJob(Long jobId, Long userId) {

        // Validate job exists
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new JobNotFoundException(jobId));

        // Fetch parsed resume from Resume Service
        String token = request.getHeader("Authorization");

        ResumeDTO resume = resumeClient.getResumeByUserId(token, userId);

        if (resume == null || resume.skills() == null) {
            throw new RuntimeException("User has no parsed resume uploaded");
        }

        List<String> jobSkills = job.getSkills(); // ✔ updated field name

        // Count skill matches
        int matches = 0;
        for (String skill : resume.skills()) {
            if (jobSkills.contains(skill)) {
                matches++;
            }
        }

        double score = jobSkills.isEmpty()
                ? 0.0
                : (double) matches / jobSkills.size() * 100.0;

        // Save job application
        JobApplication app = new JobApplication();
        app.setJobId(jobId);
        app.setUserId(userId);
        app.setResumeId(resume.id());
        app.setMatchScore(score);

        return applicationRepository.save(app);
    }
    @Override
    public List<Job> getJobsAppliedByUser(Long userId) {

        // find all applications by this user
        List<JobApplication> apps = applicationRepository.findByUserId(userId);

        // get job IDs
        List<Long> jobIds = apps.stream()
                .map(JobApplication::getJobId)
                .toList();

        // fetch jobs
        return jobRepository.findAllById(jobIds);
    }

}

