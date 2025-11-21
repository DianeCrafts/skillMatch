package com.skillmatch.microservices.job.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.skillmatch.microservices.job.model.Job;

public interface JobRepository extends JpaRepository<Job, String> {
}
