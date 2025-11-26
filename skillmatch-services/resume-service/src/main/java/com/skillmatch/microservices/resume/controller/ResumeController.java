package com.skillmatch.microservices.resume.controller;

import com.skillmatch.microservices.resume.dto.ParsedResumeDTO;
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
    @PostMapping(value = "/parse",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> parseResumeOnly(@RequestParam("file") MultipartFile file) {
        try {
            // 1. Extract text from PDF/DOCX
            String extractedText = service.parseResume(file);
            // 2. Send to AI parser
            ParsedResumeDTO parsed = service.sendToAIService(extractedText);
            // 3. Return parsed result (NOT saved yet)
            return ResponseEntity.ok(parsed);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error parsing resume: " + e.getMessage());
        }
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveParsedResume(
            @RequestParam Long userId,
            @RequestBody ParsedResumeDTO parsed
    ) {
        try {
            Resume saved = service.processAndSave(parsed, userId);
            return ResponseEntity.ok(saved);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error saving resume: " + e.getMessage());
        }
    }



    /** Get Resume for a User */
    @GetMapping("/user/{userId}")
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
