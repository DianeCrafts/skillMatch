package com.skillmatch.job.repository;

import com.skillmatch.job.entity.SavedJob;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SavedJobRepository extends JpaRepository<SavedJob, Long> {

    boolean existsByUserIdAndJob_Id(Long userId, Long jobId);

    Optional<SavedJob> findByUserIdAndJob_Id(Long userId, Long jobId);

    Page<SavedJob> findByUserId(Long userId, Pageable pageable);
}