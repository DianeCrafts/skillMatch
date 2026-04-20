package com.skillmatch.profile.service;

import com.skillmatch.profile.dto.request.AddEducationRequest;
import com.skillmatch.profile.dto.request.UpdateEducationRequest;
import com.skillmatch.profile.dto.response.EducationResponse;
import com.skillmatch.profile.entity.Education;
import com.skillmatch.profile.entity.Profile;
import com.skillmatch.profile.exception.BadRequestException;
import com.skillmatch.profile.exception.ResourceNotFoundException;
import com.skillmatch.profile.mapper.ProfileMapper;
import com.skillmatch.profile.repository.EducationRepository;
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
class EducationServiceTest {

    @Mock
    private ProfileService profileService;
    @Mock
    private EducationRepository educationRepository;
    @Mock
    private ProfileMapper profileMapper;

    @InjectMocks
    private EducationService educationService;

    @Test
    void addEducation_shouldAdd_whenValid() {
        Profile profile = TestDataFactory.profile(10L, 1L);
        AddEducationRequest request = TestDataFactory.addEducationRequest();
        Education education = TestDataFactory.education(1L, profile);

        when(profileService.getCurrentUserProfile()).thenReturn(profile);
        when(educationRepository.save(any(Education.class))).thenReturn(education);
        when(profileMapper.toEducationResponse(education)).thenReturn(EducationResponse.builder().id(1L).build());

        EducationResponse result = educationService.addEducation(request);

        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    void addEducation_shouldThrow_whenEndDateBeforeStartDate() {
        AddEducationRequest request = TestDataFactory.addEducationRequest();
        request.setStartDate(LocalDate.of(2025, 1, 1));
        request.setEndDate(LocalDate.of(2024, 1, 1));

        assertThatThrownBy(() -> educationService.addEducation(request))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("endDate cannot be before startDate");
    }

    @Test
    void updateEducation_shouldUpdate_whenFound() {
        Profile profile = TestDataFactory.profile(10L, 1L);
        Education education = TestDataFactory.education(1L, profile);
        UpdateEducationRequest request = TestDataFactory.updateEducationRequest();

        when(profileService.getCurrentUserProfile()).thenReturn(profile);
        when(educationRepository.findByIdAndProfileId(1L, 10L)).thenReturn(Optional.of(education));
        when(educationRepository.save(education)).thenReturn(education);
        when(profileMapper.toEducationResponse(education)).thenReturn(EducationResponse.builder().id(1L).build());

        EducationResponse result = educationService.updateEducation(1L, request);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(education.getSchoolName()).isEqualTo("McGill University");
    }

    @Test
    void updateEducation_shouldThrow_whenNotFound() {
        Profile profile = TestDataFactory.profile(10L, 1L);
        UpdateEducationRequest request = TestDataFactory.updateEducationRequest();

        when(profileService.getCurrentUserProfile()).thenReturn(profile);
        when(educationRepository.findByIdAndProfileId(1L, 10L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> educationService.updateEducation(1L, request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Education not found");
    }

    @Test
    void deleteEducation_shouldDelete_whenFound() {
        Profile profile = TestDataFactory.profile(10L, 1L);
        Education education = TestDataFactory.education(1L, profile);

        when(profileService.getCurrentUserProfile()).thenReturn(profile);
        when(educationRepository.findByIdAndProfileId(1L, 10L)).thenReturn(Optional.of(education));

        educationService.deleteEducation(1L);

        verify(educationRepository).delete(education);
    }

    @Test
    void deleteEducation_shouldThrow_whenNotFound() {
        Profile profile = TestDataFactory.profile(10L, 1L);

        when(profileService.getCurrentUserProfile()).thenReturn(profile);
        when(educationRepository.findByIdAndProfileId(1L, 10L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> educationService.deleteEducation(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Education not found");
    }

    @Test
    void addEducation_shouldNormalizeBlankOptionalFieldsToNull() {
        Profile profile = TestDataFactory.profile(10L, 1L);
        AddEducationRequest request = new AddEducationRequest();
        request.setSchoolName("Concordia");
        request.setDegree("  ");
        request.setFieldOfStudy(" ");
        request.setDescription(" ");

        when(profileService.getCurrentUserProfile()).thenReturn(profile);
        when(educationRepository.save(any(Education.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(profileMapper.toEducationResponse(any())).thenReturn(EducationResponse.builder().build());

        educationService.addEducation(request);

        ArgumentCaptor<Education> captor = ArgumentCaptor.forClass(Education.class);
        verify(educationRepository).save(captor.capture());

        assertThat(captor.getValue().getDegree()).isNull();
        assertThat(captor.getValue().getFieldOfStudy()).isNull();
        assertThat(captor.getValue().getDescription()).isNull();
    }
}