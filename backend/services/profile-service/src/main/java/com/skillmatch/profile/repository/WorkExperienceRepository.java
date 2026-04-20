package com.skillmatch.profile.repository;

import com.skillmatch.profile.entity.WorkExperience;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WorkExperienceRepository extends JpaRepository<WorkExperience, Long> {

    Optional<WorkExperience> findByIdAndProfileId(Long id, Long profileId);

    List<WorkExperience> findAllByProfileIdOrderBySortOrderAscIdAsc(Long profileId);
}