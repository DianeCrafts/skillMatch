package com.skillmatch.file.service.impl;

import com.skillmatch.file.config.FileStorageProperties;
import com.skillmatch.file.exception.FileStorageException;
import com.skillmatch.file.exception.ResourceNotFoundException;
import com.skillmatch.file.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class LocalFileStorageService implements FileStorageService {

    private final FileStorageProperties fileStorageProperties;
    private Path rootLocation;

    @PostConstruct
    public void init() {
        try {
            this.rootLocation = Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize();
            Files.createDirectories(this.rootLocation);
        } catch (IOException ex) {
            throw new FileStorageException("Could not initialize upload directory", ex);
        }
    }

    @Override
    public String store(MultipartFile file, Long userId, String storedFilename) {
        try {
            String safeStoredFilename = Paths.get(storedFilename).getFileName().toString();
            Path userDir = rootLocation.resolve("user-" + userId).normalize();
            Files.createDirectories(userDir);

            Path targetLocation = userDir.resolve(safeStoredFilename).normalize();

            if (!targetLocation.startsWith(userDir)) {
                throw new FileStorageException("Invalid storage path");
            }

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, targetLocation, StandardCopyOption.REPLACE_EXISTING);
            }

            return "user-" + userId + "/" + safeStoredFilename;
        } catch (IOException ex) {
            throw new FileStorageException("Failed to store file", ex);
        }
    }

    @Override
    public Resource loadAsResource(String storagePath) {
        Path filePath = resolveStoragePath(storagePath);

        try {
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                throw new ResourceNotFoundException("Stored file not found");
            }

            return resource;
        } catch (java.net.MalformedURLException ex) {
            throw new FileStorageException("Failed to load file", ex);
        }
    }

    @Override
    public void delete(String storagePath) {
        try {
            Path filePath = resolveStoragePath(storagePath);
            Files.deleteIfExists(filePath);
        } catch (IOException ex) {
            throw new FileStorageException("Failed to delete file", ex);
        }
    }

    private Path resolveStoragePath(String storagePath) {
        Objects.requireNonNull(storagePath, "storagePath must not be null");

        Path resolved = rootLocation.resolve(storagePath).normalize();

        if (!resolved.startsWith(rootLocation)) {
            throw new FileStorageException("Invalid storage path");
        }

        return resolved;
    }
}