package com.skillmatch.job.dto.request;

import com.skillmatch.job.entity.EmploymentType;
import com.skillmatch.job.entity.ExperienceLevel;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
public class CreateJobRequest {

    @NotBlank
    @Size(max = 150)
    private String title;

    @NotBlank
    private String description;

    @NotBlank
    @Size(max = 120)
    private String location;

    @NotNull
    private EmploymentType employmentType;

    @NotNull
    private ExperienceLevel experienceLevel;

    @NotEmpty
    private Set<@NotBlank String> requiredSkills;

    @Positive
    private Integer salaryMin;

    @Positive
    private Integer salaryMax;

    @NotNull
    @Future
    private LocalDate applicationDeadline;
}