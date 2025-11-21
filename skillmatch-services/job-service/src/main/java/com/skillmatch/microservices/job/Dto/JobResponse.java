package com.skillmatch.microservices.job.Dto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
@Data
@AllArgsConstructor
public class JobResponse {
    private String id;
    private String title;
    private String description;
    private List<String> skillsRequired;
    private String location;
    private boolean remote;
}
