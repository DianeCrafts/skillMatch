package com.skillmatch.microservices.job.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.skillmatch.microservices.job.model.SavedJob;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface SavedJobRepository extends JpaRepository<SavedJob, Long> {

    boolean existsByUserIdAndJobId(Long userId, Long jobId);

    void deleteByUserIdAndJobId(Long userId, Long jobId);

    List<SavedJob> findByUserId(Long userId);
}
