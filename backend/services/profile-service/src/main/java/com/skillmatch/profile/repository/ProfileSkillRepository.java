package com.skillmatch.profile.repository;

import com.skillmatch.profile.entity.ProfileSkill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileSkillRepository extends JpaRepository<ProfileSkill, Long> {

    Optional<ProfileSkill> findByIdAndProfileId(Long id, Long profileId);

    boolean existsByProfileIdAndNameIgnoreCase(Long profileId, String name);

    boolean existsByProfileIdAndNameIgnoreCaseAndIdNot(Long profileId, String name, Long id);
}