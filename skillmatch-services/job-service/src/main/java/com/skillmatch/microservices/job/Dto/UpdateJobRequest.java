package com.skillmatch.microservices.job.Dto;
import lombok.Data;

import java.util.List;
@Data
public class UpdateJobRequest {
    private String title;
    private String description;
    private List<String> skillsRequired;
    private String location;
    private boolean remote;
}
