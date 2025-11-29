package com.skillmatch.microservices.job.service;

import com.skillmatch.microservices.job.dto.CreateJobRequest;
import com.skillmatch.microservices.job.dto.UpdateJobRequest;
import com.skillmatch.microservices.job.exception.JobNotFoundException;
import com.skillmatch.microservices.job.mapper.JobMapper;
import com.skillmatch.microservices.job.model.Job;
import com.skillmatch.microservices.job.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;
    private final JobMapper jobMapper;

    @Override
    public Job createJob(Long recruiterId, CreateJobRequest request) {
        Job job = jobMapper.fromCreateRequest(request, recruiterId);
        return jobRepository.save(job);
    }

    @Override
    public Job updateJob(Long id, UpdateJobRequest request) {
        Job job = getJob(id);
        jobMapper.updateJobFromRequest(job, request);
        return jobRepository.save(job);
    }

    @Override
    public void deleteJob(Long id) {
        jobRepository.deleteById(id);
    }

    @Override
    public Job getJob(Long id) {
        return jobRepository.findById(id)
                .orElseThrow(() -> new JobNotFoundException(id));
    }

    @Override
    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    @Override
    public List<Job> getJobsByRecruiter(Long recruiterId) {
        return jobRepository.findByRecruiterId(recruiterId);
    }
}
