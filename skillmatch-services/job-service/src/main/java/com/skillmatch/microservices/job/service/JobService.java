package com.skillmatch.microservices.job.service;

import com.skillmatch.microservices.job.Dto.CreateJobRequest;
import com.skillmatch.microservices.job.Dto.UpdateJobRequest;
import org.springframework.stereotype.Service;
import com.skillmatch.microservices.job.repository.JobRepository;
import com.skillmatch.microservices.job.model.Job;

import java.util.List;
public interface JobService {
    Job createJob(String recruiterId, CreateJobRequest request);
    Job updateJob(String id, UpdateJobRequest request);
    void deleteJob(String id);
    Job getJob(String id);
}