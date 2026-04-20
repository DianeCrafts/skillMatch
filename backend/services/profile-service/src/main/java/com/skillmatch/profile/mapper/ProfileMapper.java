package com.skillmatch.profile.mapper;

import com.skillmatch.profile.dto.response.*;
import com.skillmatch.profile.entity.*;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

@Component
public class ProfileMapper {

    public ProfileResponse toProfileResponse(Profile profile) {
        return ProfileResponse.builder()
                .id(profile.getId())
                .userId(profile.getUserId())
                .firstName(profile.getFirstName())
                .lastName(profile.getLastName())
                .headline(profile.getHeadline())
                .summary(profile.getSummary())
                .location(profile.getLocation())
                .skills(profile.getSkills().stream()
                        .map(this::toSkillResponse)
                        .toList())
                .experiences(profile.getExperiences().stream()
                        .sorted(Comparator.comparing(
                                WorkExperience::getSortOrder,
                                Comparator.nullsLast(Integer::compareTo)
                        ))
                        .map(this::toWorkExperienceResponse)
                        .toList())
                .education(profile.getEducation().stream()
                        .sorted(Comparator.comparing(
                                Education::getSortOrder,
                                Comparator.nullsLast(Integer::compareTo)
                        ))
                        .map(this::toEducationResponse)
                        .toList())
                .createdAt(profile.getCreatedAt())
                .updatedAt(profile.getUpdatedAt())
                .build();
    }

    public SkillResponse toSkillResponse(ProfileSkill skill) {
        return SkillResponse.builder()
                .id(skill.getId())
                .name(skill.getName())
                .build();
    }

    public WorkExperienceResponse toWorkExperienceResponse(WorkExperience experience) {
        return WorkExperienceResponse.builder()
                .id(experience.getId())
                .jobTitle(experience.getJobTitle())
                .companyName(experience.getCompanyName())
                .location(experience.getLocation())
                .employmentType(experience.getEmploymentType())
                .startDate(experience.getStartDate())
                .endDate(experience.getEndDate())
                .currentlyWorking(experience.getCurrentlyWorking())
                .description(experience.getDescription())
                .sortOrder(experience.getSortOrder())
                .createdAt(experience.getCreatedAt())
                .updatedAt(experience.getUpdatedAt())
                .build();
    }

    public EducationResponse toEducationResponse(Education education) {
        return EducationResponse.builder()
                .id(education.getId())
                .schoolName(education.getSchoolName())
                .degree(education.getDegree())
                .fieldOfStudy(education.getFieldOfStudy())
                .startDate(education.getStartDate())
                .endDate(education.getEndDate())
                .description(education.getDescription())
                .sortOrder(education.getSortOrder())
                .createdAt(education.getCreatedAt())
                .updatedAt(education.getUpdatedAt())
                .build();
    }
}