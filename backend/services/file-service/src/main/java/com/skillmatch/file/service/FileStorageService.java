package com.skillmatch.file.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {

    /**
     * Stores file physically and returns relative storage path.
     * Example: user-5/uuid.pdf
     */
    String store(MultipartFile file, Long userId, String storedFilename);

    Resource loadAsResource(String storagePath);

    void delete(String storagePath);
}