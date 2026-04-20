package com.skillmatch.profile.repository;

import com.skillmatch.profile.entity.Education;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EducationRepository extends JpaRepository<Education, Long> {

    Optional<Education> findByIdAndProfileId(Long id, Long profileId);

    List<Education> findAllByProfileIdOrderBySortOrderAscIdAsc(Long profileId);
}