package com.skillmatch.recommendation.service;

import com.skillmatch.recommendation.dto.response.JobRecommendationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RecommendationCacheService {

    private final RedisTemplate<String, Object> redisTemplate;

    @Value("${app.cache.recommendations-ttl-minutes:10}")
    private long ttlMinutes;

    public List<JobRecommendationResponse> get(Long userId, int limit) {
        Object value = redisTemplate.opsForValue().get(key(userId, limit));

        if (value == null) {
            return null;
        }

        return (List<JobRecommendationResponse>) value;
    }

    public void put(Long userId, int limit, List<JobRecommendationResponse> recommendations) {
        redisTemplate.opsForValue().set(
                key(userId, limit),
                recommendations,
                Duration.ofMinutes(ttlMinutes)
        );
    }

    public void evictUser(Long userId) {
        Set<String> keys = redisTemplate.keys("recommendations:user:" + userId + ":limit:*");

        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }

    public void clearAllRecommendations() {
        Set<String> keys = redisTemplate.keys("recommendations:user:*");

        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }

    private String key(Long userId, int limit) {
        return "recommendations:user:%d:limit:%d".formatted(userId, limit);
    }
}