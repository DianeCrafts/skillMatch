package com.skillmatch.file.util;

import com.skillmatch.file.config.FileStorageProperties;
import com.skillmatch.file.exception.InvalidFileException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
public class FileValidator {

    private final FileStorageProperties fileStorageProperties;

    public void validateResumeFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new InvalidFileException("File must not be empty");
        }

        validateSize(file);
        validateExtension(file);
        validateContentType(file);
    }

    private void validateSize(MultipartFile file) {
        if (file.getSize() > fileStorageProperties.getMaxSizeBytes()) {
            throw new InvalidFileException("File exceeds the maximum allowed size");
        }
    }

    private void validateExtension(MultipartFile file) {
        String extension = FilenameUtils.extractExtension(file.getOriginalFilename());

        boolean allowed = fileStorageProperties.getAllowedExtensions()
                .stream()
                .anyMatch(ext -> ext.equalsIgnoreCase(extension));

        if (!allowed) {
            throw new InvalidFileException("Invalid file extension. Allowed: " + fileStorageProperties.getAllowedExtensions());
        }
    }

    private void validateContentType(MultipartFile file) {
        String contentType = file.getContentType();

        if (contentType == null || fileStorageProperties.getAllowedContentTypes()
                .stream()
                .noneMatch(type -> type.equalsIgnoreCase(contentType))) {
            throw new InvalidFileException("Invalid file content type. Allowed: " + fileStorageProperties.getAllowedContentTypes());
        }
    }
}