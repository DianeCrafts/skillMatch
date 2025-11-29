package com.skillmatch.microservices.job.service;

import com.skillmatch.microservices.job.Dto.CreateJobRequest;
import com.skillmatch.microservices.job.Dto.UpdateJobRequest;
import com.skillmatch.microservices.job.exception.JobNotFoundException;
import com.skillmatch.microservices.job.model.Job;
import com.skillmatch.microservices.job.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;

    @Override
    public Job createJob(Long recruiterId, CreateJobRequest request) {

        Job job = new Job();
        job.setRecruiterId(recruiterId);
        job.setTitle(request.getTitle());
        job.setDescription(request.getDescription());

        job.setRequirements(request.getRequirements());
        job.setLocation(request.getLocation());
        job.setSalary(request.getSalary());
        job.setExperience(request.getExperience());
        job.setSkills(request.getSkills());

        job.setRemote(request.isRemote());

        return jobRepository.save(job);
    }

    @Override
    public Job updateJob(Long id, UpdateJobRequest request) {

        Job job = getJob(id);

        job.setTitle(request.getTitle());
        job.setDescription(request.getDescription());

        job.setRequirements(request.getRequirements());
        job.setLocation(request.getLocation());
        job.setSalary(request.getSalary());
        job.setExperience(request.getExperience());
        job.setSkills(request.getSkills());

        job.setRemote(request.isRemote());

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
