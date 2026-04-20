package com.skillmatch.file.repository;


import com.skillmatch.file.entity.ResumeFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ResumeFileRepository extends JpaRepository<ResumeFile, Long> {
    Optional<ResumeFile> findByUserId(Long userId);
    boolean existsByUserId(Long userId);
}