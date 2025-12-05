package com.skillmatch.microservices.job.service;

import com.skillmatch.microservices.job.model.Job;
import com.skillmatch.microservices.job.model.SavedJob;
import com.skillmatch.microservices.job.repository.JobRepository;
import com.skillmatch.microservices.job.repository.SavedJobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SavedJobService {

    private final SavedJobRepository savedJobRepository;
    private final JobRepository jobRepository;

    public void saveJob(Long userId, Long jobId) {
        if (!jobRepository.existsById(jobId)) {
            throw new RuntimeException("Job not found");
        }

        if (!savedJobRepository.existsByUserIdAndJobId(userId, jobId)) {
            savedJobRepository.save(new SavedJob(null, userId, jobId, LocalDateTime.now()));
        }
    }
    @Transactional
    public void unsaveJob(Long userId, Long jobId) {
        savedJobRepository.deleteByUserIdAndJobId(userId, jobId);
    }

    public List<Job> getSavedJobs(Long userId) {
        List<SavedJob> saved = savedJobRepository.findByUserId(userId);

        List<Long> jobIds = saved
                .stream()
                .map(SavedJob::getJobId)
                .toList();

        return jobRepository.findAllById(jobIds);
    }
}
