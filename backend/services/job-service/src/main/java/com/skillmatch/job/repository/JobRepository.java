package com.skillmatch.job.repository;

import com.skillmatch.job.entity.Job;
import com.skillmatch.job.entity.JobStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface JobRepository extends JpaRepository<Job, Long>, JpaSpecificationExecutor<Job> {

    Page<Job> findByRecruiterId(Long recruiterId, Pageable pageable);

    Optional<Job> findByIdAndStatus(Long id, JobStatus status);
}