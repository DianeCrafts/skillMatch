package com.skillmatch.microservices.resume.service;

import com.skillmatch.microservices.resume.model.Resume;
import com.skillmatch.microservices.resume.repository.ResumeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResumeService {
    private final ResumeRepository repo;

    public ResumeService(ResumeRepository repo) {
        this.repo = repo;
    }

    public Resume saveResume(Resume resume) {
        return repo.save(resume);
    }

    public Resume getResumeByUser(Long userId) {
        return repo.findByUserId(userId);
    }

    public List<Resume> getAllResumes() {
        return repo.findAll();
    }
}
