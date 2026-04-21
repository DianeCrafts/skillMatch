package com.skillmatch.application.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skillmatch.application.dto.request.ApplyRequest;
import com.skillmatch.application.dto.request.UpdateApplicationStatusRequest;
import com.skillmatch.application.dto.response.ApplicationResponse;
import com.skillmatch.application.entity.ApplicationStatus;
import com.skillmatch.application.security.JwtAuthenticationFilter;
import com.skillmatch.application.service.ApplicationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(ApplicationController.class)
class ApplicationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ApplicationService applicationService;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void apply_shouldReturn200() throws Exception {
        ApplyRequest request = new ApplyRequest();
        request.setJobId(1L);

        ApplicationResponse response = ApplicationResponse.builder()
                .id(1L)
                .jobId(1L)
                .applicantId(2L)
                .status(ApplicationStatus.APPLIED)
                .build();

        when(applicationService.apply(any(ApplyRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/applications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("APPLIED"));
    }

    @Test
    void getMyApplications_shouldReturn200() throws Exception {
        ApplicationResponse response = ApplicationResponse.builder()
                .id(1L)
                .jobId(1L)
                .applicantId(2L)
                .status(ApplicationStatus.APPLIED)
                .build();

        when(applicationService.getMyApplications()).thenReturn(List.of(response));

        mockMvc.perform(get("/api/applications/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void getApplicationsByJob_shouldReturnPagedResult() throws Exception {
        ApplicationResponse response = ApplicationResponse.builder()
                .id(1L)
                .jobId(1L)
                .applicantId(2L)
                .status(ApplicationStatus.APPLIED)
                .build();

        when(applicationService.getApplicationsByJob(eq(1L), any()))
                .thenReturn(new PageImpl<>(List.of(response), PageRequest.of(0, 20), 1));

        mockMvc.perform(get("/api/applications/job/1")
                        .param("page", "0")
                        .param("size", "20")
                        .param("sort", "appliedAt,desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void updateStatus_shouldReturn200() throws Exception {
        UpdateApplicationStatusRequest request = new UpdateApplicationStatusRequest();
        request.setStatus(ApplicationStatus.UNDER_REVIEW);

        ApplicationResponse response = ApplicationResponse.builder()
                .id(1L)
                .jobId(1L)
                .applicantId(2L)
                .status(ApplicationStatus.UNDER_REVIEW)
                .build();

        when(applicationService.updateApplicationStatus(eq(1L), any(UpdateApplicationStatusRequest.class)))
                .thenReturn(response);

        mockMvc.perform(patch("/api/applications/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UNDER_REVIEW"));
    }

    @Test
    void withdraw_shouldReturn200() throws Exception {
        ApplicationResponse response = ApplicationResponse.builder()
                .id(1L)
                .status(ApplicationStatus.WITHDRAWN)
                .build();

        when(applicationService.withdraw(1L)).thenReturn(response);

        mockMvc.perform(patch("/api/applications/1/withdraw"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("WITHDRAWN"));
    }
}