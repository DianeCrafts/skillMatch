package com.skillmatch.microservices.resume.repository;

import com.skillmatch.microservices.resume.model.Experience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ExperienceRepository extends JpaRepository<Experience, Long> {

    // Get all experiences for a given resume
    List<Experience> findByResumeId(Long resumeId);
}
