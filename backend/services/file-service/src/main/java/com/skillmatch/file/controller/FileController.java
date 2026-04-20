package com.skillmatch.file.controller;

import com.skillmatch.file.dto.response.ResumeFileResponse;
import com.skillmatch.file.service.ResumeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class FileController {

    private final ResumeService resumeService;

    @PostMapping(value = "/resume", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload or replace current user's resume")
    public ResponseEntity<ResumeFileResponse> uploadResume(
            @RequestBody(
                    required = true,
                    content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)
            )
            @RequestParam("file")
            @Parameter(description = "Resume PDF file")
            MultipartFile file
    ) {
        return ResponseEntity.ok(resumeService.uploadResume(file));
    }

    @GetMapping("/resume")
    @Operation(summary = "Get current user's resume metadata")
    public ResponseEntity<ResumeFileResponse> getMyResumeMetadata() {
        return ResponseEntity.ok(resumeService.getMyResumeMetadata());
    }

    @GetMapping("/resume/download")
    @Operation(summary = "Download current user's resume")
    public ResponseEntity<org.springframework.core.io.Resource> downloadMyResume() {
        ResumeService.ResumeDownload download = resumeService.downloadMyResume();

        MediaType mediaType;
        try {
            mediaType = MediaType.parseMediaType(download.contentType());
        } catch (Exception ex) {
            mediaType = MediaType.APPLICATION_OCTET_STREAM;
        }

        return ResponseEntity.ok()
                .contentType(mediaType)
                .contentLength(download.size())
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment()
                                .filename(download.originalFilename())
                                .build()
                                .toString()
                )
                .body(download.resource());
    }

    @DeleteMapping("/resume")
    @Operation(summary = "Delete current user's resume")
    public ResponseEntity<Void> deleteMyResume() {
        resumeService.deleteMyResume();
        return ResponseEntity.noContent().build();
    }
}