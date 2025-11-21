package com.skillmatch.microservices.job.Dto;

import lombok.Data;

@Data
public class EducationData {
    private Long id;
    private String degree;
    private String institution;
    private String startDate;
    private String endDate;
}
