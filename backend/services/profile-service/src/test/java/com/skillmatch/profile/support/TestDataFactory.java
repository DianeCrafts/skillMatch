package com.skillmatch.profile.support;

import com.skillmatch.profile.dto.request.*;
import com.skillmatch.profile.entity.Education;
import com.skillmatch.profile.entity.Profile;
import com.skillmatch.profile.entity.ProfileSkill;
import com.skillmatch.profile.entity.WorkExperience;

import java.time.LocalDate;
import java.util.ArrayList;

public final class TestDataFactory {

    private TestDataFactory() {
    }

    public static Profile profile(Long id, Long userId) {
        Profile profile = new Profile();
        profile.setId(id);
        profile.setUserId(userId);
        profile.setFirstName("Diane");
        profile.setLastName("Crafts");
        profile.setHeadline("Backend Developer");
        profile.setSummary("Summary");
        profile.setLocation("Montreal");
        profile.setSkills(new ArrayList<>());
        profile.setExperiences(new ArrayList<>());
        profile.setEducation(new ArrayList<>());
        return profile;
    }

    public static CreateProfileRequest createProfileRequest() {
        CreateProfileRequest request = new CreateProfileRequest();
        request.setFirstName("Diane");
        request.setLastName("Crafts");
        request.setHeadline("Backend Developer");
        request.setSummary("Summary");
        request.setLocation("Montreal");
        return request;
    }

    public static UpdateProfileRequest updateProfileRequest() {
        UpdateProfileRequest request = new UpdateProfileRequest();
        request.setFirstName("Updated");
        request.setLastName("User");
        request.setHeadline("Updated Headline");
        request.setSummary("Updated Summary");
        request.setLocation("Toronto");
        return request;
    }

    public static AddSkillRequest addSkillRequest(String name) {
        AddSkillRequest request = new AddSkillRequest();
        request.setName(name);
        return request;
    }

    public static UpdateSkillRequest updateSkillRequest(String name) {
        UpdateSkillRequest request = new UpdateSkillRequest();
        request.setName(name);
        return request;
    }

    public static ProfileSkill skill(Long id, Profile profile, String name) {
        ProfileSkill skill = new ProfileSkill();
        skill.setId(id);
        skill.setProfile(profile);
        skill.setName(name);
        return skill;
    }

    public static AddEducationRequest addEducationRequest() {
        AddEducationRequest request = new AddEducationRequest();
        request.setSchoolName("Concordia University");
        request.setDegree("BSc");
        request.setFieldOfStudy("Computer Science");
        request.setStartDate(LocalDate.of(2022, 9, 1));
        request.setEndDate(LocalDate.of(2026, 5, 1));
        request.setDescription("Education description");
        request.setSortOrder(1);
        return request;
    }

    public static UpdateEducationRequest updateEducationRequest() {
        UpdateEducationRequest request = new UpdateEducationRequest();
        request.setSchoolName("McGill University");
        request.setDegree("MSc");
        request.setFieldOfStudy("Software Engineering");
        request.setStartDate(LocalDate.of(2023, 1, 1));
        request.setEndDate(LocalDate.of(2025, 1, 1));
        request.setDescription("Updated education");
        request.setSortOrder(2);
        return request;
    }

    public static Education education(Long id, Profile profile) {
        Education education = new Education();
        education.setId(id);
        education.setProfile(profile);
        education.setSchoolName("Concordia University");
        education.setDegree("BSc");
        education.setFieldOfStudy("Computer Science");
        education.setStartDate(LocalDate.of(2022, 9, 1));
        education.setEndDate(LocalDate.of(2026, 5, 1));
        education.setDescription("Education description");
        education.setSortOrder(1);
        return education;
    }

    public static AddWorkExperienceRequest addWorkExperienceRequest() {
        AddWorkExperienceRequest request = new AddWorkExperienceRequest();
        request.setJobTitle("Backend Intern");
        request.setCompanyName("Tech Corp");
        request.setLocation("Remote");
        request.setEmploymentType("Internship");
        request.setStartDate(LocalDate.of(2025, 1, 1));
        request.setEndDate(LocalDate.of(2025, 6, 1));
        request.setCurrentlyWorking(false);
        request.setDescription("Built APIs");
        request.setSortOrder(1);
        return request;
    }

    public static UpdateWorkExperienceRequest updateWorkExperienceRequest() {
        UpdateWorkExperienceRequest request = new UpdateWorkExperienceRequest();
        request.setJobTitle("Junior Backend Developer");
        request.setCompanyName("Tech Corp");
        request.setLocation("Montreal");
        request.setEmploymentType("Full-time");
        request.setStartDate(LocalDate.of(2025, 1, 1));
        request.setEndDate(null);
        request.setCurrentlyWorking(true);
        request.setDescription("Updated experience");
        request.setSortOrder(2);
        return request;
    }

    public static WorkExperience workExperience(Long id, Profile profile) {
        WorkExperience experience = new WorkExperience();
        experience.setId(id);
        experience.setProfile(profile);
        experience.setJobTitle("Backend Intern");
        experience.setCompanyName("Tech Corp");
        experience.setLocation("Remote");
        experience.setEmploymentType("Internship");
        experience.setStartDate(LocalDate.of(2025, 1, 1));
        experience.setEndDate(LocalDate.of(2025, 6, 1));
        experience.setCurrentlyWorking(false);
        experience.setDescription("Built APIs");
        experience.setSortOrder(1);
        return experience;
    }
}