package com.skillmatch.application.repository;

import com.skillmatch.application.entity.Application;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

    boolean existsByJobIdAndApplicantId(Long jobId, Long applicantId);

    @EntityGraph(attributePaths = {"statusHistory"})
    List<Application> findByApplicantIdOrderByAppliedAtDesc(Long applicantId);

    @EntityGraph(attributePaths = {"statusHistory"})
    Page<Application> findByJobId(Long jobId, Pageable pageable);

    @EntityGraph(attributePaths = {"statusHistory"})
    Optional<Application> findWithStatusHistoryById(Long id);

    Optional<Application> findByIdAndApplicantId(Long id, Long applicantId);
}