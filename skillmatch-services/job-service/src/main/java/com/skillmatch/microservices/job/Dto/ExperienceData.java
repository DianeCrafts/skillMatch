package com.skillmatch.microservices.job.Dto;

import lombok.Data;

@Data
public class ExperienceData {
    private Long id;
    private String company;
    private String position;
    private String description;
    private String startDate;
    private String endDate;
}
