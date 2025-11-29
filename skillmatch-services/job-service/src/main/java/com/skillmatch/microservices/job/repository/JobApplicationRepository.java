package com.skillmatch.microservices.job.repository;

import com.skillmatch.microservices.job.model.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {
    List<JobApplication> findByJobId(Long jobId);
    List<JobApplication> findByUserId(Long userId);

}
