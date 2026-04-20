package com.skillmatch.job.dto.response;

import com.skillmatch.job.entity.EmploymentType;
import com.skillmatch.job.entity.ExperienceLevel;
import com.skillmatch.job.entity.JobStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@Builder
public class JobResponse {
    private Long id;
    private Long recruiterId;
    private String title;
    private String description;
    private String location;
    private EmploymentType employmentType;
    private ExperienceLevel experienceLevel;
    private Set<String> requiredSkills;
    private Integer salaryMin;
    private Integer salaryMax;
    private LocalDate applicationDeadline;
    private JobStatus status;
    private boolean saved;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}