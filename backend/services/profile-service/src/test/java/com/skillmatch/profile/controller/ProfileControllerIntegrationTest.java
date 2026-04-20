package com.skillmatch.profile.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skillmatch.profile.dto.request.CreateProfileRequest;
import com.skillmatch.profile.dto.request.UpdateProfileRequest;
import com.skillmatch.profile.repository.ProfileRepository;
import com.skillmatch.profile.support.TestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ProfileControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProfileRepository profileRepository;

    @MockBean
    private JwtDecoder jwtDecoder;

    @BeforeEach
    void setUp() {
        profileRepository.deleteAll();

        Jwt jwt = new Jwt(
                "fake-token",
                Instant.now(),
                Instant.now().plusSeconds(3600),
                Map.of("alg", "HS256"),
                Map.of(
                        "userId", 1L,
                        "role", "JOB_SEEKER",
                        "sub", "test@example.com"
                )
        );

        when(jwtDecoder.decode(anyString())).thenReturn(jwt);
    }

    @Test
    void createProfile_shouldReturn201() throws Exception {
        CreateProfileRequest request = TestDataFactory.createProfileRequest();

        mockMvc.perform(post("/api/profiles/me")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer fake-token")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.firstName").value("Diane"));
    }

    @Test
    void createProfile_shouldReturn409_whenDuplicate() throws Exception {
        CreateProfileRequest request = TestDataFactory.createProfileRequest();

        mockMvc.perform(post("/api/profiles/me")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer fake-token")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/profiles/me")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer fake-token")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }

    @Test
    void getMyProfile_shouldReturn200_whenExists() throws Exception {
        CreateProfileRequest request = TestDataFactory.createProfileRequest();

        mockMvc.perform(post("/api/profiles/me")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer fake-token")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/profiles/me")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer fake-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1L));
    }

    @Test
    void getMyProfile_shouldReturn404_whenMissing() throws Exception {
        mockMvc.perform(get("/api/profiles/me")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer fake-token"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateProfile_shouldReturn200_whenExists() throws Exception {
        CreateProfileRequest create = TestDataFactory.createProfileRequest();
        UpdateProfileRequest update = TestDataFactory.updateProfileRequest();

        mockMvc.perform(post("/api/profiles/me")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer fake-token")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(create)))
                .andExpect(status().isCreated());

        mockMvc.perform(put("/api/profiles/me")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer fake-token")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Updated"))
                .andExpect(jsonPath("$.location").value("Toronto"));
    }

    @Test
    void createProfile_shouldReturn401_whenNoToken() throws Exception {
        CreateProfileRequest request = TestDataFactory.createProfileRequest();

        mockMvc.perform(post("/api/profiles/me")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }
}