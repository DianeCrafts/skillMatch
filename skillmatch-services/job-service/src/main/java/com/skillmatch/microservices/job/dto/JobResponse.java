package com.skillmatch.microservices.job.dto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
@Data
@AllArgsConstructor
public class JobResponse {
    private Long id;
    private String title;
    private String description;
//    private String companyName;
    private List<String> requirements;
    private String location;

    private String salary;
    private String experience;

    private List<String> skills;
    private boolean remote;
}