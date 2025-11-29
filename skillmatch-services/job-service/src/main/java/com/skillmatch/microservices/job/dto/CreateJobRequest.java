package com.skillmatch.microservices.job.dto;

import lombok.Data;

import java.util.List;


@Data
public class CreateJobRequest {
    private String title;
    private String description;
//    private String companyName;
    private List<String> requirements;
    private String location;

    private String salary;        // Added
    private String experience;    // Added

    private List<String> skills;
    private boolean remote;
}