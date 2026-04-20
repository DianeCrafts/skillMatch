package com.skillmatch.profile.service;

import com.skillmatch.profile.dto.request.AddEducationRequest;
import com.skillmatch.profile.dto.request.UpdateEducationRequest;
import com.skillmatch.profile.dto.response.EducationResponse;
import com.skillmatch.profile.entity.Education;
import com.skillmatch.profile.entity.Profile;
import com.skillmatch.profile.exception.BadRequestException;
import com.skillmatch.profile.exception.ResourceNotFoundException;
import com.skillmatch.profile.mapper.ProfileMapper;
import com.skillmatch.profile.repository.EducationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EducationService {

    private final ProfileService profileService;
    private final EducationRepository educationRepository;
    private final ProfileMapper profileMapper;

    @Transactional
    public EducationResponse addEducation(AddEducationRequest request) {
        validateDates(request.getStartDate(), request.getEndDate());

        Profile profile = profileService.getCurrentUserProfile();

        Education education = new Education();
        education.setProfile(profile);
        applyEducationFields(education, request.getSchoolName(), request.getDegree(), request.getFieldOfStudy(),
                request.getStartDate(), request.getEndDate(), request.getDescription(), request.getSortOrder());

        return profileMapper.toEducationResponse(educationRepository.save(education));
    }

    @Transactional
    public EducationResponse updateEducation(Long educationId, UpdateEducationRequest request) {
        validateDates(request.getStartDate(), request.getEndDate());

        Profile profile = profileService.getCurrentUserProfile();

        Education education = educationRepository.findByIdAndProfileId(educationId, profile.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Education not found"));

        applyEducationFields(education, request.getSchoolName(), request.getDegree(), request.getFieldOfStudy(),
                request.getStartDate(), request.getEndDate(), request.getDescription(), request.getSortOrder());

        return profileMapper.toEducationResponse(educationRepository.save(education));
    }

    @Transactional
    public void deleteEducation(Long educationId) {
        Profile profile = profileService.getCurrentUserProfile();

        Education education = educationRepository.findByIdAndProfileId(educationId, profile.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Education not found"));

        educationRepository.delete(education);
    }

    private void validateDates(java.time.LocalDate startDate, java.time.LocalDate endDate) {
        if (startDate != null && endDate != null && endDate.isBefore(startDate)) {
            throw new BadRequestException("endDate cannot be before startDate");
        }
    }

    private void applyEducationFields(Education education,
                                      String schoolName,
                                      String degree,
                                      String fieldOfStudy,
                                      java.time.LocalDate startDate,
                                      java.time.LocalDate endDate,
                                      String description,
                                      Integer sortOrder) {
        education.setSchoolName(schoolName.trim());
        education.setDegree(normalize(degree));
        education.setFieldOfStudy(normalize(fieldOfStudy));
        education.setStartDate(startDate);
        education.setEndDate(endDate);
        education.setDescription(normalize(description));
        education.setSortOrder(sortOrder);
    }

    private String normalize(String value) {
        if (value == null) return null;
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}