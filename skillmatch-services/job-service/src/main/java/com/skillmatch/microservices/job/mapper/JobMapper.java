package com.skillmatch.microservices.job.mapper;



import com.skillmatch.microservices.job.dto.*;
import com.skillmatch.microservices.job.model.Job;
import org.springframework.stereotype.Component;

@Component
public class JobMapper {

    public JobResponse toResponse(Job job) {
        return new JobResponse(
                job.getId(),
                job.getTitle(),
                job.getDescription(),
                job.getRequirements(),
                job.getLocation(),
                job.getSalary(),
                job.getExperience(),
                job.getSkills(),
                job.isRemote()
        );
    }

    public Job fromCreateRequest(CreateJobRequest req, Long recruiterId) {
        Job job = new Job();
        job.setRecruiterId(recruiterId);
        job.setTitle(req.getTitle());
        job.setDescription(req.getDescription());
        job.setRequirements(req.getRequirements());
        job.setLocation(req.getLocation());
        job.setSalary(req.getSalary());
        job.setExperience(req.getExperience());
        job.setSkills(req.getSkills());
        job.setRemote(req.isRemote());
        return job;
    }

    public void updateJobFromRequest(Job job, UpdateJobRequest req) {
        job.setTitle(req.getTitle());
        job.setDescription(req.getDescription());
        job.setRequirements(req.getRequirements());
        job.setLocation(req.getLocation());
        job.setSalary(req.getSalary());
        job.setExperience(req.getExperience());
        job.setSkills(req.getSkills());
        job.setRemote(req.isRemote());
    }
}
