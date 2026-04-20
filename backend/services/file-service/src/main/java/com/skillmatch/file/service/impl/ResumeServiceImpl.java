package com.skillmatch.file.service.impl;

import com.skillmatch.file.dto.response.ResumeFileResponse;
import com.skillmatch.file.entity.ResumeFile;
import com.skillmatch.file.exception.ResourceNotFoundException;
import com.skillmatch.file.repository.ResumeFileRepository;
import com.skillmatch.file.security.SecurityUtils;
import com.skillmatch.file.service.FileStorageService;
import com.skillmatch.file.service.ResumeService;
import com.skillmatch.file.util.FileValidator;
import com.skillmatch.file.util.FilenameUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional
public class ResumeServiceImpl implements ResumeService {

    private final ResumeFileRepository resumeFileRepository;
    private final FileStorageService fileStorageService;
    private final FileValidator fileValidator;

    @Override
    public ResumeFileResponse uploadResume(MultipartFile file) {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        String currentRole = SecurityUtils.getCurrentUserRole();

        validateJobSeekerRole(currentRole);
        fileValidator.validateResumeFile(file);

        ResumeFile existingResume = resumeFileRepository.findByUserId(currentUserId).orElse(null);

        if (existingResume != null) {
            fileStorageService.delete(existingResume.getStoragePath());
        }

        String storedFilename = FilenameUtils.generateStoredFilename(file.getOriginalFilename());
        String storagePath = fileStorageService.store(file, currentUserId, storedFilename);

        ResumeFile resumeFile = existingResume != null ? existingResume : new ResumeFile();
        resumeFile.setUserId(currentUserId);
        resumeFile.setOriginalFilename(file.getOriginalFilename());
        resumeFile.setStoredFilename(storedFilename);
        resumeFile.setContentType(file.getContentType());
        resumeFile.setSize(file.getSize());
        resumeFile.setStoragePath(storagePath);

        ResumeFile saved = resumeFileRepository.save(resumeFile);
        return mapToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public ResumeFileResponse getMyResumeMetadata() {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        String currentRole = SecurityUtils.getCurrentUserRole();

        validateJobSeekerRole(currentRole);

        ResumeFile resumeFile = getResumeByUserId(currentUserId);
        return mapToResponse(resumeFile);
    }

    @Override
    @Transactional(readOnly = true)
    public ResumeDownload downloadMyResume() {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        String currentRole = SecurityUtils.getCurrentUserRole();

        validateJobSeekerRole(currentRole);

        ResumeFile resumeFile = getResumeByUserId(currentUserId);
        Resource resource = fileStorageService.loadAsResource(resumeFile.getStoragePath());

        return new ResumeDownload(
                resource,
                resumeFile.getOriginalFilename(),
                resumeFile.getContentType(),
                resumeFile.getSize()
        );
    }

    @Override
    public void deleteMyResume() {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        String currentRole = SecurityUtils.getCurrentUserRole();

        validateJobSeekerRole(currentRole);

        ResumeFile resumeFile = getResumeByUserId(currentUserId);
        fileStorageService.delete(resumeFile.getStoragePath());
        resumeFileRepository.delete(resumeFile);
    }

    private ResumeFile getResumeByUserId(Long userId) {
        return resumeFileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found for current user"));
    }

    private void validateJobSeekerRole(String role) {
        if (!"JOB_SEEKER".equals(role)) {
            throw new AccessDeniedException("Only JOB_SEEKER users can manage resumes");
        }
    }

    private ResumeFileResponse mapToResponse(ResumeFile resumeFile) {
        return ResumeFileResponse.builder()
                .id(resumeFile.getId())
                .userId(resumeFile.getUserId())
                .originalFilename(resumeFile.getOriginalFilename())
                .contentType(resumeFile.getContentType())
                .size(resumeFile.getSize())
                .uploadedAt(resumeFile.getUploadedAt())
                .updatedAt(resumeFile.getUpdatedAt())
                .build();
    }
}