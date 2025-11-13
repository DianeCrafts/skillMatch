package com.skillmatch.microservices.resume.repository;

import com.skillmatch.microservices.resume.model.Education;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EducationRepository extends JpaRepository<Education, Long> {

    // Get all education records for a given resume
    List<Education> findByResumeId(Long resumeId);
}
