package com.skillmatch.microservices.job.Dto;
import lombok.Data;

import java.util.List;
@Data
public class UpdateJobRequest {
    private String title;
    private String description;

    private List<String> requirements;
    private String location;

    private String salary;
    private String experience;

    private List<String> skills;
    private boolean remote;
}