package com.skillmatch.microservices.resume.controller;

import com.skillmatch.microservices.resume.model.Resume;
import com.skillmatch.microservices.resume.service.ResumeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/resumes")
public class ResumeController {

    private final ResumeService service;

    public ResumeController(ResumeService service) {
        this.service = service;
    }

    @PostMapping
    public Resume create(@RequestBody Resume resume) {
        return service.saveResume(resume);
    }

    @GetMapping("/{userId}")
    public Resume getByUser(@PathVariable Long userId) {
        return service.getResumeByUser(userId);
    }

    @GetMapping
    public List<Resume> getAll() {
        return service.getAllResumes();
    }
}
