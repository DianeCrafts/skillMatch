package com.skillmatch.microservices.job.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import com.google.gson.Gson;
import com.skillmatch.microservices.job.ai.AiClient;
import com.skillmatch.microservices.job.dto.CreateJobRequest;
import com.skillmatch.microservices.job.dto.UpdateJobRequest;
import com.skillmatch.microservices.job.exception.JobNotFoundException;
import com.skillmatch.microservices.job.mapper.JobMapper;
import com.skillmatch.microservices.job.model.Job;
import com.skillmatch.microservices.job.repository.JobRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;
    private final JobMapper jobMapper;
    private final ElasticsearchClient esClient;
    private final AiClient aiClient;
    private final ResumeClient resumeClient;
    private final JobSearchService jobSearchService;
    private final HttpServletRequest request;
    @Override
    public Job createJob(Long recruiterId, CreateJobRequest request) {
        Job job = jobMapper.fromCreateRequest(request, recruiterId);

        String text = job.getTitle() + " " +
                job.getDescription() + " " +
                String.join(" ", job.getSkills());

        float[] embedding = aiClient.getEmbedding(text);
        job.setEmbeddingJson(new Gson().toJson(embedding));

        Job saved = jobRepository.save(job);

        indexJobInES(saved, embedding); // <-- pass embedding

        return saved;
    }


    @Override
    public Job updateJob(Long id, UpdateJobRequest request) {
        Job job = getJob(id);

        // 1. Update fields from request
        jobMapper.updateJobFromRequest(job, request);

        // 2. Rebuild text for embedding
        String text = job.getTitle() + " " +
                job.getDescription() + " " +
                String.join(" ", job.getSkills());

        // 3. Generate new embedding
        float[] embedding = aiClient.getEmbedding(text);
        job.setEmbeddingJson(new Gson().toJson(embedding));

        // 4. Save updated job
        Job updated = jobRepository.save(job);

        // 5. Update Elasticsearch
        indexJobInES(updated, embedding);

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

    private void indexJobInES(Job job, float[] embedding) {
        try {
            esClient.index(i -> i
                    .index("jobs")
                    .id(job.getId().toString())
                    .document(Map.of(
                            "id", job.getId(),
                            "title", job.getTitle(),
                            "description", job.getDescription(),
                            "skills", job.getSkills(),
                            "embedding", embedding
                    ))
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
    @Override
    public List<Job> recommendJobs(Long userId) {
        String token = request.getHeader("Authorization");
        float[] userVec = resumeClient.getUserEmbedding(userId, token);
        return jobSearchService.searchByVector(userVec);
    }


}
