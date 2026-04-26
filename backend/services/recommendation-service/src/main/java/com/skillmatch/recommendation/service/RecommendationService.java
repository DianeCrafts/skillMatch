package com.skillmatch.recommendation.service;
import com.skillmatch.recommendation.dto.response.*;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.skillmatch.recommendation.service.ProfileServiceClient;
import com.skillmatch.recommendation.service.JobServiceClient;
import com.skillmatch.recommendation.service.EmbeddingServiceClient;

import com.skillmatch.recommendation.entity.UserEmbedding;
import com.skillmatch.recommendation.entity.JobEmbedding;

import com.skillmatch.recommendation.repository.UserEmbeddingRepository;
import com.skillmatch.recommendation.repository.JobEmbeddingRepository;
@Service
@RequiredArgsConstructor
public class RecommendationService {

    private final ProfileServiceClient profileClient;
    private final JobServiceClient jobClient;
    private final EmbeddingServiceClient embeddingClient;

    private final UserEmbeddingRepository userRepo;
    private final JobEmbeddingRepository jobRepo;

    public List<JobRecommendationResponse> recommendJobsForUser(Long userId, int limit) {

        UserEmbedding userEmbedding = userRepo.findById(userId)
                .orElseGet(() -> recomputeAndSaveUserEmbedding(userId));

        List<Object[]> results = jobRepo.findMostSimilarJobs(
                userEmbedding.getEmbedding(),
                limit
        );

        List<Long> jobIds = results.stream()
                .map(r -> ((Number) r[0]).longValue())
                .toList();

        List<JobDataResponse> jobs = jobClient.getJobsByIds(jobIds);

        Map<Long, Double> scoreMap = results.stream()
                .collect(Collectors.toMap(
                        r -> ((Number) r[0]).longValue(),
                        r -> ((Number) r[1]).doubleValue()
                ));

        return jobs.stream()
                .map(job -> new JobRecommendationResponse(
                        job.id(),
                        job.title(),
                        null,
                        job.location(),
                        scoreMap.get(job.id())
                ))
                .toList();
    }

    public void recomputeUserEmbedding(Long userId) {
        recomputeAndSaveUserEmbedding(userId);
    }

    public void recomputeJobEmbedding(Long jobId) {
        JobDataResponse job = jobClient.getJobById(jobId);

        String text = buildJobText(job);
        List<Double> embedding = embeddingClient.generateEmbedding(text);

        LocalDateTime now = LocalDateTime.now();
        String vector = toVectorString(embedding);

        jobRepo.upsertJobEmbedding(jobId, text, vector, now);
    }
    private UserEmbedding recomputeAndSaveUserEmbedding(Long userId) {
        ProfileDataResponse profile = profileClient.getProfileByUserId(userId);
        String text = buildProfileText(profile);

        List<Double> embedding = embeddingClient.generateEmbedding(text);

        LocalDateTime now = LocalDateTime.now();
        String vector = toVectorString(embedding);

        userRepo.upsertUserEmbedding(userId, text, vector, now);

        UserEmbedding entity = new UserEmbedding();
        entity.setUserId(userId);
        entity.setProfileText(text);
        entity.setEmbedding(vector);
        entity.setUpdatedAt(now);

        return entity;
    }

    private String buildProfileText(ProfileDataResponse p) {
        return "Summary: " + p.summary() +
                " Skills: " + p.skills().stream()
                .map(SkillDto::name)
                .collect(Collectors.joining(", ")) +
                " Experience: " + p.experiences().stream()
                .map(ExperienceDto::description)
                .collect(Collectors.joining(". "));
    }

    private String buildJobText(JobDataResponse j) {
        String skills = j.requiredSkills() == null
                ? ""
                : String.join(", ", j.requiredSkills());

        return "Title: " + j.title() +
                " Description: " + j.description() +
                " Location: " + j.location() +
                " Skills: " + skills;
    }

    private String toVectorString(List<Double> embedding) {
        return "[" + embedding.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(",")) + "]";
    }
}