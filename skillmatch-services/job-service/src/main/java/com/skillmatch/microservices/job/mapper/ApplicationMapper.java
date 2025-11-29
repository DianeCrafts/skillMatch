package com.skillmatch.microservices.job.mapper;


import com.skillmatch.microservices.job.dto.ApplicationResponse;
import com.skillmatch.microservices.job.model.JobApplication;
import org.springframework.stereotype.Component;

@Component
public class ApplicationMapper {

    public ApplicationResponse toResponse(JobApplication app, String userName) {
        return new ApplicationResponse(
                app.getId(),
                app.getUserId(),
                userName,
                app.getResumeId(),
                app.getMatchScore(),
                app.getStatus()
        );
    }
}