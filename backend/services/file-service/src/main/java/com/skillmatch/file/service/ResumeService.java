package com.skillmatch.file.service;

import com.skillmatch.file.dto.response.ResumeFileResponse;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface ResumeService {

    ResumeFileResponse uploadResume(MultipartFile file);

    ResumeFileResponse getMyResumeMetadata();

    ResumeDownload downloadMyResume();

    void deleteMyResume();

    record ResumeDownload(
            Resource resource,
            String originalFilename,
            String contentType,
            long size
    ) {}
}