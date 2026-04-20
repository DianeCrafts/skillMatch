package com.skillmatch.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skillmatch.auth.dto.request.LoginRequest;
import com.skillmatch.auth.dto.request.RegisterRequest;
import com.skillmatch.auth.entity.User;
import com.skillmatch.auth.enums.UserRole;
import com.skillmatch.auth.repository.UserRepository;
import com.skillmatch.auth.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void register_shouldReturn201_whenRequestIsValid() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setFirstName("Sara");
        request.setLastName("Ali");
        request.setEmail("sara@example.com");
        request.setPassword("StrongPass123");
        request.setRole(UserRole.JOB_SEEKER);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.email").value("sara@example.com"));
    }

    @Test
    void login_shouldReturn200_whenCredentialsAreValid() throws Exception {
        User user = new User();
        user.setFirstName("Sara");
        user.setLastName("Ali");
        user.setEmail("sara@example.com");
        user.setPassword(passwordEncoder.encode("StrongPass123"));
        user.setRole(UserRole.JOB_SEEKER);

        userRepository.save(user);

        LoginRequest request = new LoginRequest();
        request.setEmail("sara@example.com");
        request.setPassword("StrongPass123");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.token").exists());
    }

    @Test
    void me_shouldReturn200_whenTokenIsValid() throws Exception {
        User user = new User();
        user.setFirstName("Sara");
        user.setLastName("Ali");
        user.setEmail("sara@example.com");
        user.setPassword(passwordEncoder.encode("StrongPass123"));
        user.setRole(UserRole.JOB_SEEKER);

        User savedUser = userRepository.save(user);
        String token = jwtService.generateToken(new com.skillmatch.auth.security.CustomUserDetails(savedUser));

        mockMvc.perform(get("/api/auth/me")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.email").value("sara@example.com"));
    }

    @Test
    void me_shouldReturn4xx_whenTokenIsMissing() throws Exception {
        mockMvc.perform(get("/api/auth/me"))
                .andExpect(status().is4xxClientError());
    }
}