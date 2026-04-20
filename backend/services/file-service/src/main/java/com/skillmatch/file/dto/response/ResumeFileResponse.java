package com.skillmatch.file.dto.response;

import java.time.LocalDateTime;



import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class ResumeFileResponse {
    private Long id;
    private Long userId;
    private String originalFilename;
    private String contentType;
    private Long size;
    private LocalDateTime uploadedAt;
    private LocalDateTime updatedAt;
}