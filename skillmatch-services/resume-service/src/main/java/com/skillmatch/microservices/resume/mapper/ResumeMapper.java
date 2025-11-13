package com.skillmatch.microservices.resume.mapper;

import com.skillmatch.microservices.resume.dto.ai.ParsedResumeDTO;
import com.skillmatch.microservices.resume.model.*;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ResumeMapper {

    public Resume toEntity(ParsedResumeDTO parsed, Long userId) {

        Resume resume = new Resume();
        resume.setUserId(userId);
        resume.setTitle("Extracted Resume");
        resume.setSummary(parsed.summary());

        // EDUCATION
        if (parsed.education() != null) {
            resume.setEducation(parsed.education().stream().map(edu -> {
                Education e = new Education();
                e.setInstitution(edu.institution());
                e.setDegree(edu.degree());
                e.setField(edu.field());
                e.setStartDate(parseSqlDate(edu.startDate()));
                e.setEndDate(parseSqlDate(edu.endDate()));
                e.setResume(resume);
                return e;
            }).collect(Collectors.toList()));
        }

        // EXPERIENCE
        if (parsed.experience() != null) {
            resume.setExperience(parsed.experience().stream().map(exp -> {
                Experience e = new Experience();
                e.setCompany(exp.company());
                e.setPosition(exp.position());
                e.setDescription(exp.description());
                e.setStartDate(parseSqlDate(exp.startDate()));
                e.setEndDate(parseSqlDate(exp.endDate()));
                e.setResume(resume);
                return e;
            }).collect(Collectors.toList()));
        }

        // SKILLS
        if (parsed.skills() != null) {
            resume.setSkills(parsed.skills().stream().map(skillName -> {
                Skill s = new Skill();
                s.setName(skillName);
                s.setResume(resume);
                return s;
            }).collect(Collectors.toList()));
        }

        return resume;
    }

    private java.sql.Date parseSqlDate(String text) {
        if (text == null) return null;
        return java.sql.Date.valueOf(text); // works because AI returns YYYY-MM-DD
    }
}
