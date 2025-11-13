package com.skillmatch.microservices.resume.controller;

import com.skillmatch.microservices.resume.dto.ai.ParsedResumeDTO;
import com.skillmatch.microservices.resume.model.Resume;
import com.skillmatch.microservices.resume.service.ResumeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/resumes")
public class ResumeController {

    private final ResumeService service;

    public ResumeController(ResumeService service) {
        this.service = service;
    }

    /**  Upload Resume File */
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadResume(
            @RequestParam("file") MultipartFile file,
            @RequestParam("userId") Long userId) {
        try {
            // 1. Extract text
            String extractedText = service.parseResume(file);
            // 2. Send to AI service
            ParsedResumeDTO parsed = service.sendToAIService(extractedText);
            System.out.println(parsed);
            // 3. Map and save
            Resume saved = service.processAndSave(parsed, userId);
            // 4. Return parsed info for user preview
            return  ResponseEntity.ok(saved);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error processing resume: " + e.getMessage());
        }
    }

    /** Get Resume for a User */
    @GetMapping("/{userId}")
    public ResponseEntity<?> getResumeByUser(@PathVariable Long userId) {
        Resume resume = service.getByUser(userId);
        return resume != null ? ResponseEntity.ok(resume)
                : ResponseEntity.notFound().build();
    }

    /** Update Resume (after user corrections) */
    @PutMapping("/{resumeId}")
    public ResponseEntity<Resume> updateResume(@PathVariable Long resumeId, @RequestBody Resume updated) {
        return ResponseEntity.ok(service.updateResume(resumeId, updated));
    }
}
