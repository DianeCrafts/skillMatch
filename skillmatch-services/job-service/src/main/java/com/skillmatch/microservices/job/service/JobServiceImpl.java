package com.skillmatch.microservices.job.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import com.skillmatch.microservices.job.dto.CreateJobRequest;
import com.skillmatch.microservices.job.dto.UpdateJobRequest;
import com.skillmatch.microservices.job.exception.JobNotFoundException;
import com.skillmatch.microservices.job.mapper.JobMapper;
import com.skillmatch.microservices.job.model.Job;
import com.skillmatch.microservices.job.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;
    private final JobMapper jobMapper;
    private final ElasticsearchClient esClient;

    @Override
    public Job createJob(Long recruiterId, CreateJobRequest request) {
        Job job = jobMapper.fromCreateRequest(request, recruiterId);
        Job saved = jobRepository.save(job);

        indexJobInES(saved);

        return saved;
    }

    @Override
    public Job updateJob(Long id, UpdateJobRequest request) {
        Job job = getJob(id);
        jobMapper.updateJobFromRequest(job, request);
        Job updated = jobRepository.save(job);

        indexJobInES(updated);  // update ES version too

        return updated;
    }

    @Override
    public void deleteJob(Long id) {
        jobRepository.deleteById(id);
        deleteJobFromES(id); // remove from Elasticsearch
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

    // -------------------------------------------
    //  Elasticsearch helper methods
    // -------------------------------------------

    private void indexJobInES(Job job) {
        try {
            esClient.index(i -> i
                    .index("jobs")
                    .id(job.getId().toString())
                    .document(job)
            );
        } catch (IOException e) {
            System.err.println("Failed to index job in Elasticsearch: " + e.getMessage());
        }
    }

    private void deleteJobFromES(Long jobId) {
        try {
            esClient.delete(d -> d
                    .index("jobs")
                    .id(jobId.toString())
            );
        } catch (IOException e) {
            System.err.println("Failed to delete job from Elasticsearch: " + e.getMessage());
        }
    }
}
