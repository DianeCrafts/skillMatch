package com.skillmatch.recommendation.service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.skillmatch.recommendation.dto.response.ProfileDataResponse;
@Service
@RequiredArgsConstructor
public class ProfileServiceClient {

    private final RestClient profileRestClient;

    public ProfileDataResponse getProfileByUserId(Long userId) {
        try {
            return profileRestClient.get()
                    .uri("/api/profiles/internal/user/{userId}", userId)
                    .retrieve()
                    .body(ProfileDataResponse.class);

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}