package com.skillmatch.microservices.job.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skillmatch.microservices.job.dto.CreateJobRequest;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;
import com.fasterxml.jackson.core.type.TypeReference;


@Service
public class JobSeeder {

    private final JobService jobService;
    private final ObjectMapper objectMapper;

    public JobSeeder(JobService jobService, ObjectMapper objectMapper) {
        this.jobService = jobService;
        this.objectMapper = objectMapper;
    }

    public void seedFromFile(String filePath) {
        try (InputStream is = getClass().getClassLoader()
                .getResourceAsStream(filePath)) {

            if (is == null) {
                throw new RuntimeException("Seed file not found: " + filePath);
            }

            List<CreateJobRequest> jobs =
                    objectMapper.readValue(is, new TypeReference<>() {});

            Long recruiterId = 1L;

            // repeat 10 times to make dataset of 1000
            for (int round = 0; round < 10; round++) {
                for (CreateJobRequest request : jobs) {
                    jobService.createJob(recruiterId, request);
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to seed jobs", e);
        }
    }
}
