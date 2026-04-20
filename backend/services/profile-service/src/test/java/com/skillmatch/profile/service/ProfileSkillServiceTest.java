package com.skillmatch.profile.service;

import com.skillmatch.profile.dto.request.AddSkillRequest;
import com.skillmatch.profile.dto.request.UpdateSkillRequest;
import com.skillmatch.profile.dto.response.SkillResponse;
import com.skillmatch.profile.entity.Profile;
import com.skillmatch.profile.entity.ProfileSkill;
import com.skillmatch.profile.exception.DuplicateResourceException;
import com.skillmatch.profile.exception.ResourceNotFoundException;
import com.skillmatch.profile.mapper.ProfileMapper;
import com.skillmatch.profile.repository.ProfileSkillRepository;
import com.skillmatch.profile.support.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProfileSkillServiceTest {

    @Mock
    private ProfileService profileService;
    @Mock
    private ProfileSkillRepository profileSkillRepository;
    @Mock
    private ProfileMapper profileMapper;

    @InjectMocks
    private ProfileSkillService profileSkillService;

    @Test
    void addSkill_shouldAddSkill_whenUnique() {
        Profile profile = TestDataFactory.profile(10L, 1L);
        AddSkillRequest request = TestDataFactory.addSkillRequest("Spring Boot");
        ProfileSkill savedSkill = TestDataFactory.skill(1L, profile, "Spring Boot");
        SkillResponse expected = SkillResponse.builder().id(1L).name("Spring Boot").build();

        when(profileService.getCurrentUserProfile()).thenReturn(profile);
        when(profileSkillRepository.existsByProfileIdAndNameIgnoreCase(10L, "Spring Boot")).thenReturn(false);
        when(profileSkillRepository.save(any(ProfileSkill.class))).thenReturn(savedSkill);
        when(profileMapper.toSkillResponse(savedSkill)).thenReturn(expected);

        SkillResponse result = profileSkillService.addSkill(request);

        assertThat(result).isEqualTo(expected);
    }

    @Test
    void addSkill_shouldTrimName() {
        Profile profile = TestDataFactory.profile(10L, 1L);
        AddSkillRequest request = TestDataFactory.addSkillRequest("  Spring Boot  ");
        ProfileSkill savedSkill = TestDataFactory.skill(1L, profile, "Spring Boot");

        when(profileService.getCurrentUserProfile()).thenReturn(profile);
        when(profileSkillRepository.existsByProfileIdAndNameIgnoreCase(10L, "Spring Boot")).thenReturn(false);
        when(profileSkillRepository.save(any(ProfileSkill.class))).thenReturn(savedSkill);
        when(profileMapper.toSkillResponse(savedSkill)).thenReturn(SkillResponse.builder().build());

        profileSkillService.addSkill(request);

        ArgumentCaptor<ProfileSkill> captor = ArgumentCaptor.forClass(ProfileSkill.class);
        verify(profileSkillRepository).save(captor.capture());
        assertThat(captor.getValue().getName()).isEqualTo("Spring Boot");
    }

    @Test
    void addSkill_shouldThrow_whenDuplicate() {
        Profile profile = TestDataFactory.profile(10L, 1L);
        AddSkillRequest request = TestDataFactory.addSkillRequest("Spring Boot");

        when(profileService.getCurrentUserProfile()).thenReturn(profile);
        when(profileSkillRepository.existsByProfileIdAndNameIgnoreCase(10L, "Spring Boot")).thenReturn(true);

        assertThatThrownBy(() -> profileSkillService.addSkill(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("Skill already exists");
    }

    @Test
    void updateSkill_shouldUpdate_whenFoundAndUnique() {
        Profile profile = TestDataFactory.profile(10L, 1L);
        ProfileSkill skill = TestDataFactory.skill(1L, profile, "Java");
        UpdateSkillRequest request = TestDataFactory.updateSkillRequest("Spring Boot");
        SkillResponse expected = SkillResponse.builder().id(1L).name("Spring Boot").build();

        when(profileService.getCurrentUserProfile()).thenReturn(profile);
        when(profileSkillRepository.findByIdAndProfileId(1L, 10L)).thenReturn(Optional.of(skill));
        when(profileSkillRepository.existsByProfileIdAndNameIgnoreCase(10L, "Spring Boot")).thenReturn(false);
        when(profileSkillRepository.save(skill)).thenReturn(skill);
        when(profileMapper.toSkillResponse(skill)).thenReturn(expected);

        SkillResponse result = profileSkillService.updateSkill(1L, request);

        assertThat(result).isEqualTo(expected);
        assertThat(skill.getName()).isEqualTo("Spring Boot");
    }

    @Test
    void updateSkill_shouldAllowSameNameDifferentCase() {
        Profile profile = TestDataFactory.profile(10L, 1L);
        ProfileSkill skill = TestDataFactory.skill(1L, profile, "Spring Boot");
        UpdateSkillRequest request = TestDataFactory.updateSkillRequest("spring boot");

        when(profileService.getCurrentUserProfile()).thenReturn(profile);
        when(profileSkillRepository.findByIdAndProfileId(1L, 10L)).thenReturn(Optional.of(skill));
        when(profileSkillRepository.save(skill)).thenReturn(skill);
        when(profileMapper.toSkillResponse(skill)).thenReturn(SkillResponse.builder().build());

        profileSkillService.updateSkill(1L, request);

        verify(profileSkillRepository, never()).existsByProfileIdAndNameIgnoreCase(anyLong(), anyString());
    }

    @Test
    void updateSkill_shouldThrow_whenSkillNotFound() {
        Profile profile = TestDataFactory.profile(10L, 1L);
        UpdateSkillRequest request = TestDataFactory.updateSkillRequest("Spring Boot");

        when(profileService.getCurrentUserProfile()).thenReturn(profile);
        when(profileSkillRepository.findByIdAndProfileId(1L, 10L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> profileSkillService.updateSkill(1L, request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Skill not found");
    }

    @Test
    void updateSkill_shouldThrow_whenDuplicateTargetName() {
        Profile profile = TestDataFactory.profile(10L, 1L);
        ProfileSkill skill = TestDataFactory.skill(1L, profile, "Java");
        UpdateSkillRequest request = TestDataFactory.updateSkillRequest("Spring Boot");

        when(profileService.getCurrentUserProfile()).thenReturn(profile);
        when(profileSkillRepository.findByIdAndProfileId(1L, 10L)).thenReturn(Optional.of(skill));
        when(profileSkillRepository.existsByProfileIdAndNameIgnoreCase(10L, "Spring Boot")).thenReturn(true);

        assertThatThrownBy(() -> profileSkillService.updateSkill(1L, request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("Skill already exists");
    }

    @Test
    void deleteSkill_shouldDelete_whenFound() {
        Profile profile = TestDataFactory.profile(10L, 1L);
        ProfileSkill skill = TestDataFactory.skill(1L, profile, "Spring Boot");

        when(profileService.getCurrentUserProfile()).thenReturn(profile);
        when(profileSkillRepository.findByIdAndProfileId(1L, 10L)).thenReturn(Optional.of(skill));

        profileSkillService.deleteSkill(1L);

        verify(profileSkillRepository).delete(skill);
    }

    @Test
    void deleteSkill_shouldThrow_whenMissing() {
        Profile profile = TestDataFactory.profile(10L, 1L);

        when(profileService.getCurrentUserProfile()).thenReturn(profile);
        when(profileSkillRepository.findByIdAndProfileId(1L, 10L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> profileSkillService.deleteSkill(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Skill not found");
    }
}