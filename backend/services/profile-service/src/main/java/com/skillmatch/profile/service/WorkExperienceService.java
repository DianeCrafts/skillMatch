package com.skillmatch.profile.service;

import com.skillmatch.profile.dto.request.AddWorkExperienceRequest;
import com.skillmatch.profile.dto.request.UpdateWorkExperienceRequest;
import com.skillmatch.profile.dto.response.WorkExperienceResponse;
import com.skillmatch.profile.entity.Profile;
import com.skillmatch.profile.entity.WorkExperience;
import com.skillmatch.profile.exception.BadRequestException;
import com.skillmatch.profile.exception.ResourceNotFoundException;
import com.skillmatch.profile.mapper.ProfileMapper;
import com.skillmatch.profile.repository.WorkExperienceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WorkExperienceService {

    private final ProfileService profileService;
    private final WorkExperienceRepository workExperienceRepository;
    private final ProfileMapper profileMapper;

    @Transactional
    public WorkExperienceResponse addExperience(AddWorkExperienceRequest request) {
        validateDates(request.getStartDate(), request.getEndDate(), request.getCurrentlyWorking());

        Profile profile = profileService.getCurrentUserProfile();

        WorkExperience experience = new WorkExperience();
        experience.setProfile(profile);
        applyExperienceFields(experience, request.getJobTitle(), request.getCompanyName(), request.getLocation(),
                request.getEmploymentType(), request.getStartDate(), request.getEndDate(),
                request.getCurrentlyWorking(), request.getDescription(), request.getSortOrder());

        return profileMapper.toWorkExperienceResponse(workExperienceRepository.save(experience));
    }

    @Transactional
    public WorkExperienceResponse updateExperience(Long experienceId, UpdateWorkExperienceRequest request) {
        validateDates(request.getStartDate(), request.getEndDate(), request.getCurrentlyWorking());

        Profile profile = profileService.getCurrentUserProfile();

        WorkExperience experience = workExperienceRepository.findByIdAndProfileId(experienceId, profile.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Work experience not found"));

        applyExperienceFields(experience, request.getJobTitle(), request.getCompanyName(), request.getLocation(),
                request.getEmploymentType(), request.getStartDate(), request.getEndDate(),
                request.getCurrentlyWorking(), request.getDescription(), request.getSortOrder());

        return profileMapper.toWorkExperienceResponse(workExperienceRepository.save(experience));
    }

    @Transactional
    public void deleteExperience(Long experienceId) {
        Profile profile = profileService.getCurrentUserProfile();

        WorkExperience experience = workExperienceRepository.findByIdAndProfileId(experienceId, profile.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Work experience not found"));

        workExperienceRepository.delete(experience);
    }

    private void validateDates(java.time.LocalDate startDate, java.time.LocalDate endDate, Boolean currentlyWorking) {
        if (endDate != null && endDate.isBefore(startDate)) {
            throw new BadRequestException("endDate cannot be before startDate");
        }

        if (Boolean.TRUE.equals(currentlyWorking) && endDate != null) {
            throw new BadRequestException("endDate should be null when currentlyWorking is true");
        }
    }

    private void applyExperienceFields(WorkExperience experience,
                                       String jobTitle,
                                       String companyName,
                                       String location,
                                       String employmentType,
                                       java.time.LocalDate startDate,
                                       java.time.LocalDate endDate,
                                       Boolean currentlyWorking,
                                       String description,
                                       Integer sortOrder) {
        experience.setJobTitle(jobTitle.trim());
        experience.setCompanyName(companyName.trim());
        experience.setLocation(normalize(location));
        experience.setEmploymentType(normalize(employmentType));
        experience.setStartDate(startDate);
        experience.setEndDate(endDate);
        experience.setCurrentlyWorking(currentlyWorking);
        experience.setDescription(normalize(description));
        experience.setSortOrder(sortOrder);
    }

    private String normalize(String value) {
        if (value == null) return null;
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}