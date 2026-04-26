package com.skillmatch.recommendation.service;

import com.skillmatch.recommendation.dto.response.ExperienceDto;
import com.skillmatch.recommendation.dto.response.JobDataResponse;
import com.skillmatch.recommendation.dto.response.JobRecommendationResponse;
import com.skillmatch.recommendation.dto.response.ProfileDataResponse;
import com.skillmatch.recommendation.dto.response.SkillDto;
import com.skillmatch.recommendation.entity.UserEmbedding;
import com.skillmatch.recommendation.repository.JobEmbeddingRepository;
import com.skillmatch.recommendation.repository.UserEmbeddingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecommendationService {

    private final ProfileServiceClient profileClient;
    private final JobServiceClient jobClient;
    private final EmbeddingServiceClient embeddingClient;
    private final RecommendationCacheService cacheService;

    private final UserEmbeddingRepository userRepo;
    private final JobEmbeddingRepository jobRepo;

    public List<JobRecommendationResponse> recommendJobsForUser(Long userId, int limit) {

        List<JobRecommendationResponse> cached = cacheService.get(userId, limit);
        if (cached != null) {
            return cached;
        }

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

        List<JobRecommendationResponse> recommendations = jobs.stream()
                .map(job -> new JobRecommendationResponse(
                        job.id(),
                        job.title(),
                        null,
                        job.location(),
                        scoreMap.get(job.id())
                ))
                .toList();

        cacheService.put(userId, limit, recommendations);

        return recommendations;
    }

    public void recomputeUserEmbedding(Long userId) {
        recomputeAndSaveUserEmbedding(userId);
        cacheService.evictUser(userId);
    }

    public void recomputeJobEmbedding(Long jobId) {
        JobDataResponse job = jobClient.getJobById(jobId);

        String text = buildJobText(job);
        List<Double> embedding = embeddingClient.generateEmbedding(text);

        LocalDateTime now = LocalDateTime.now();
        String vector = toVectorString(embedding);

        jobRepo.upsertJobEmbedding(jobId, text, vector, now);

        // Simple invalidation strategy for now
        cacheService.clearAllRecommendations();
    }

    private UserEmbedding recomputeAndSaveUserEmbedding(Long userId) {
        ProfileDataResponse profile = profileClient.getProfileByUserId(userId);
        String text = buildProfileText(profile);

        List<Double> embedding = embeddingClient.generateEmbedding(text);

        LocalDateTime now = LocalDateTime.now();
        String vector = toVectorString(embedding);

        userRepo.upsertUserEmbedding(userId, text, vector, now);
        cacheService.evictUser(userId);

        UserEmbedding entity = new UserEmbedding();
        entity.setUserId(userId);
        entity.setProfileText(text);
        entity.setEmbedding(vector);
        entity.setUpdatedAt(now);

        return entity;
    }

    private String buildProfileText(ProfileDataResponse p) {
        String skills = p.skills() == null
                ? ""
                : p.skills().stream()
                .map(SkillDto::name)
                .collect(Collectors.joining(", "));

        String experience = p.experiences() == null
                ? ""
                : p.experiences().stream()
                .map(ExperienceDto::description)
                .collect(Collectors.joining(". "));

        return "Summary: " + p.summary() +
                " Skills: " + skills +
                " Experience: " + experience;
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