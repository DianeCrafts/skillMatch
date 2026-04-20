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

@Data
@Builder
public class JobSummaryResponse {
    private Long id;
    private String title;
    private String location;
    private EmploymentType employmentType;
    private ExperienceLevel experienceLevel;
    private Integer salaryMin;
    private Integer salaryMax;
    private LocalDate applicationDeadline;
    private boolean saved;
    private LocalDateTime createdAt;
}