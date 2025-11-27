package com.skillmatch.microservices.resume.mapper;

import com.skillmatch.microservices.resume.dto.EducationDTO;
import com.skillmatch.microservices.resume.dto.ExperienceDTO;
import com.skillmatch.microservices.resume.dto.ResumeDTO;
import com.skillmatch.microservices.resume.model.*;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ResumeMapper {

    public Resume toEntity(ResumeDTO parsed, Long userId) {

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

    public ResumeDTO toDTO(Resume resume) {
        return new ResumeDTO(
                resume.getId(),
                resume.getSummary(),
                null,   // name (if you decide to include)
                null,   // email
                null,   // phone
                resume.getEducation().stream()
                        .map(e -> new EducationDTO(
                                e.getInstitution(),
                                e.getDegree(),
                                e.getField(),
                                e.getStartDate() != null ? e.getStartDate().toString() : null,
                                e.getEndDate() != null ? e.getEndDate().toString() : null
                        )).toList(),
                resume.getExperience().stream()
                        .map(e -> new ExperienceDTO(
                                e.getCompany(),
                                e.getPosition(),
                                e.getStartDate() != null ? e.getStartDate().toString() : null,
                                e.getEndDate() != null ? e.getEndDate().toString() : null,
                                e.getDescription()
                        )).toList(),
                resume.getSkills().stream()
                        .map(Skill::getName)
                        .toList()
        );
    }

    public void mergeIntoExisting(Resume existing, ResumeDTO dto) {

        existing.setSummary(dto.summary());

        // EDUCATION
        existing.getEducation().clear();
        if (dto.education() != null) {
            existing.getEducation().addAll(
                    dto.education().stream().map(edu -> {
                        Education e = new Education();
                        e.setInstitution(edu.institution());
                        e.setDegree(edu.degree());
                        e.setField(edu.field());
                        e.setStartDate(parseSqlDate(edu.startDate()));
                        e.setEndDate(parseSqlDate(edu.endDate()));
                        e.setResume(existing);
                        return e;
                    }).toList()
            );
        }

        // EXPERIENCE
        existing.getExperience().clear();
        if (dto.experience() != null) {
            existing.getExperience().addAll(
                    dto.experience().stream().map(exp -> {
                        Experience e = new Experience();
                        e.setCompany(exp.company());
                        e.setPosition(exp.position());
                        e.setDescription(exp.description());
                        e.setStartDate(parseSqlDate(exp.startDate()));
                        e.setEndDate(parseSqlDate(exp.endDate()));
                        e.setResume(existing);
                        return e;
                    }).toList()
            );
        }

        // SKILLS
        existing.getSkills().clear();
        if (dto.skills() != null) {
            existing.getSkills().addAll(
                    dto.skills().stream().map(s -> {
                        Skill skill = new Skill();
                        skill.setName(s);
                        skill.setResume(existing);
                        return skill;
                    }).toList()
            );
        }
    }


    private java.sql.Date parseSqlDate(String text) {
        if (text == null) return null;
        return java.sql.Date.valueOf(text); // works because AI returns YYYY-MM-DD
    }
}
