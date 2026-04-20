package com.skillmatch.profile.service;

import com.skillmatch.profile.dto.request.AddSkillRequest;
import com.skillmatch.profile.dto.request.UpdateSkillRequest;
import com.skillmatch.profile.dto.response.SkillResponse;
import com.skillmatch.profile.entity.Profile;
import com.skillmatch.profile.entity.ProfileSkill;
import com.skillmatch.profile.exception.DuplicateResourceException;
import com.skillmatch.profile.exception.ResourceNotFoundException;
import com.skillmatch.profile.mapper.ProfileMapper;
import com.skillmatch.profile.repository.ProfileSkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProfileSkillService {

    private final ProfileService profileService;
    private final ProfileSkillRepository profileSkillRepository;
    private final ProfileMapper profileMapper;

    @Transactional
    public SkillResponse addSkill(AddSkillRequest request) {
        Profile profile = profileService.getCurrentUserProfile();
        String normalizedName = normalizeRequired(request.getName());

        if (profileSkillRepository.existsByProfileIdAndNameIgnoreCase(profile.getId(), normalizedName)) {
            throw new DuplicateResourceException("Skill already exists");
        }

        ProfileSkill skill = new ProfileSkill();
        skill.setProfile(profile);
        skill.setName(normalizedName);

        return profileMapper.toSkillResponse(profileSkillRepository.save(skill));
    }

    @Transactional
    public SkillResponse updateSkill(Long skillId, UpdateSkillRequest request) {
        Profile profile = profileService.getCurrentUserProfile();
        String normalizedName = normalizeRequired(request.getName());

        ProfileSkill skill = profileSkillRepository.findByIdAndProfileId(skillId, profile.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Skill not found"));

        if (!skill.getName().equalsIgnoreCase(normalizedName)
                && profileSkillRepository.existsByProfileIdAndNameIgnoreCase(profile.getId(), normalizedName)) {
            throw new DuplicateResourceException("Skill already exists");
        }

        skill.setName(normalizedName);
        return profileMapper.toSkillResponse(profileSkillRepository.save(skill));
    }

    @Transactional
    public void deleteSkill(Long skillId) {
        Profile profile = profileService.getCurrentUserProfile();

        ProfileSkill skill = profileSkillRepository.findByIdAndProfileId(skillId, profile.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Skill not found"));

        profileSkillRepository.delete(skill);
    }

    private String normalizeRequired(String value) {
        return value.trim();
    }
}