package com.skillmatch.microservices.resume.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.skillmatch.microservices.resume.model.Resume;

public interface ResumeRepository extends JpaRepository<Resume, Long> {
    Resume findByUserId(Long userId);
}
