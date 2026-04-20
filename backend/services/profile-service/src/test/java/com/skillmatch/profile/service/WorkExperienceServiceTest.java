package com.skillmatch.profile.service;

import com.skillmatch.profile.dto.request.AddWorkExperienceRequest;
import com.skillmatch.profile.dto.request.UpdateWorkExperienceRequest;
import com.skillmatch.profile.dto.response.WorkExperienceResponse;
import com.skillmatch.profile.entity.Profile;
import com.skillmatch.profile.entity.WorkExperience;
import com.skillmatch.profile.exception.BadRequestException;
import com.skillmatch.profile.exception.ResourceNotFoundException;
import com.skillmatch.profile.mapper.ProfileMapper;
import com.skillmatch.profile.repository.WorkExperienceRepository;
import com.skillmatch.profile.support.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WorkExperienceServiceTest {

    @Mock
    private ProfileService profileService;
    @Mock
    private WorkExperienceRepository workExperienceRepository;
    @Mock
    private ProfileMapper profileMapper;

    @InjectMocks
    private WorkExperienceService workExperienceService;

    @Test
    void addExperience_shouldAdd_whenValid() {
        Profile profile = TestDataFactory.profile(10L, 1L);
        AddWorkExperienceRequest request = TestDataFactory.addWorkExperienceRequest();
        WorkExperience experience = TestDataFactory.workExperience(1L, profile);

        when(profileService.getCurrentUserProfile()).thenReturn(profile);
        when(workExperienceRepository.save(any(WorkExperience.class))).thenReturn(experience);
        when(profileMapper.toWorkExperienceResponse(experience)).thenReturn(WorkExperienceResponse.builder().id(1L).build());

        WorkExperienceResponse result = workExperienceService.addExperience(request);

        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    void addExperience_shouldThrow_whenEndDateBeforeStartDate() {
        AddWorkExperienceRequest request = TestDataFactory.addWorkExperienceRequest();
        request.setStartDate(LocalDate.of(2025, 5, 1));
        request.setEndDate(LocalDate.of(2025, 1, 1));

        assertThatThrownBy(() -> workExperienceService.addExperience(request))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("endDate cannot be before startDate");
    }

    @Test
    void addExperience_shouldThrow_whenCurrentlyWorkingAndEndDatePresent() {
        AddWorkExperienceRequest request = TestDataFactory.addWorkExperienceRequest();
        request.setCurrentlyWorking(true);
        request.setEndDate(LocalDate.of(2025, 6, 1));

        assertThatThrownBy(() -> workExperienceService.addExperience(request))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("endDate should be null when currentlyWorking is true");
    }

    @Test
    void updateExperience_shouldUpdate_whenFound() {
        Profile profile = TestDataFactory.profile(10L, 1L);
        WorkExperience experience = TestDataFactory.workExperience(1L, profile);
        UpdateWorkExperienceRequest request = TestDataFactory.updateWorkExperienceRequest();

        when(profileService.getCurrentUserProfile()).thenReturn(profile);
        when(workExperienceRepository.findByIdAndProfileId(1L, 10L)).thenReturn(Optional.of(experience));
        when(workExperienceRepository.save(experience)).thenReturn(experience);
        when(profileMapper.toWorkExperienceResponse(experience)).thenReturn(WorkExperienceResponse.builder().id(1L).build());

        WorkExperienceResponse result = workExperienceService.updateExperience(1L, request);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(experience.getCurrentlyWorking()).isTrue();
        assertThat(experience.getEndDate()).isNull();
    }

    @Test
    void updateExperience_shouldThrow_whenNotFound() {
        Profile profile = TestDataFactory.profile(10L, 1L);
        UpdateWorkExperienceRequest request = TestDataFactory.updateWorkExperienceRequest();

        when(profileService.getCurrentUserProfile()).thenReturn(profile);
        when(workExperienceRepository.findByIdAndProfileId(1L, 10L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> workExperienceService.updateExperience(1L, request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Work experience not found");
    }

    @Test
    void deleteExperience_shouldDelete_whenFound() {
        Profile profile = TestDataFactory.profile(10L, 1L);
        WorkExperience experience = TestDataFactory.workExperience(1L, profile);

        when(profileService.getCurrentUserProfile()).thenReturn(profile);
        when(workExperienceRepository.findByIdAndProfileId(1L, 10L)).thenReturn(Optional.of(experience));

        workExperienceService.deleteExperience(1L);

        verify(workExperienceRepository).delete(experience);
    }

    @Test
    void deleteExperience_shouldThrow_whenMissing() {
        Profile profile = TestDataFactory.profile(10L, 1L);

        when(profileService.getCurrentUserProfile()).thenReturn(profile);
        when(workExperienceRepository.findByIdAndProfileId(1L, 10L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> workExperienceService.deleteExperience(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Work experience not found");
    }

    @Test
    void addExperience_shouldNormalizeBlankOptionalFieldsToNull() {
        Profile profile = TestDataFactory.profile(10L, 1L);
        AddWorkExperienceRequest request = new AddWorkExperienceRequest();
        request.setJobTitle("Backend Developer");
        request.setCompanyName("Tech Corp");
        request.setStartDate(LocalDate.of(2025, 1, 1));
        request.setCurrentlyWorking(false);
        request.setLocation(" ");
        request.setEmploymentType(" ");
        request.setDescription(" ");

        when(profileService.getCurrentUserProfile()).thenReturn(profile);
        when(workExperienceRepository.save(any(WorkExperience.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(profileMapper.toWorkExperienceResponse(any())).thenReturn(WorkExperienceResponse.builder().build());

        workExperienceService.addExperience(request);

        ArgumentCaptor<WorkExperience> captor = ArgumentCaptor.forClass(WorkExperience.class);
        verify(workExperienceRepository).save(captor.capture());

        assertThat(captor.getValue().getLocation()).isNull();
        assertThat(captor.getValue().getEmploymentType()).isNull();
        assertThat(captor.getValue().getDescription()).isNull();
    }
}