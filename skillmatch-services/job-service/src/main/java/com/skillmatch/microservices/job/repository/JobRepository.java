package com.skillmatch.microservices.job.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.skillmatch.microservices.job.model.Job;

import java.util.List;

public interface JobRepository extends JpaRepository<Job, Long> {
    List<Job> findByRecruiterId(Long recruiterId);
}
