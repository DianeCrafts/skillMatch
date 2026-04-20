package com.skillmatch.profile.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UpdateWorkExperienceRequest {

    @NotBlank
    @Size(max = 150)
    private String jobTitle;

    @NotBlank
    @Size(max = 150)
    private String companyName;

    @Size(max = 255)
    private String location;

    @Size(max = 100)
    private String employmentType;

    @NotNull
    private LocalDate startDate;

    private LocalDate endDate;

    @NotNull
    private Boolean currentlyWorking;

    @Size(max = 3000)
    private String description;

    private Integer sortOrder;
}