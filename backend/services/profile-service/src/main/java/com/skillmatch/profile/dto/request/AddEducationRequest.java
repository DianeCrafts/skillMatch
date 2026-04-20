package com.skillmatch.profile.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class AddEducationRequest {

    @NotBlank
    @Size(max = 150)
    private String schoolName;

    @Size(max = 150)
    private String degree;

    @Size(max = 150)
    private String fieldOfStudy;

    private LocalDate startDate;
    private LocalDate endDate;

    @Size(max = 3000)
    private String description;

    private Integer sortOrder;
}