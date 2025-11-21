package com.skillmatch.microservices.job.Dto;

import lombok.Data;

import java.util.List;

@Data
public class ResumeData {
    private Long id;
    private Long userId;
    private String title;
    private String summary;
    private List<SkillData> skills;
    private List<EducationData> education;
    private List<ExperienceData> experience;
}
