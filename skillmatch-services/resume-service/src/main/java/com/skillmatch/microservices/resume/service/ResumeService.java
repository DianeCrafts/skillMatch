package com.skillmatch.microservices.resume.service;

import com.skillmatch.microservices.resume.dto.ai.AIResponse;
import com.skillmatch.microservices.resume.dto.ai.ParsedResumeDTO;
import com.skillmatch.microservices.resume.dto.ai.ResumeParseRequest;
import com.skillmatch.microservices.resume.mapper.ResumeMapper;
import com.skillmatch.microservices.resume.model.*;
import com.skillmatch.microservices.resume.repository.ResumeRepository;
import org.apache.tika.Tika;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;


@Service
public class ResumeService {

    private final ResumeRepository repo;
    private final ResumeMapper resumeMapper;
    private final WebClient webClient;
    public ResumeService(ResumeRepository repo, WebClient aiWebClient, ResumeMapper resumeMapper) {
        this.repo = repo;
        this.webClient = aiWebClient;
        this.resumeMapper = resumeMapper;
    }

    /** Step 1: Extract text from PDF/DOCX */
    public String parseResume(MultipartFile file) throws Exception {
        Tika tika = new Tika();
        return tika.parseToString(file.getInputStream());
    }

    /** Step 2: Send extracted text to AIService */
    public ParsedResumeDTO sendToAIService(String text) {
        return webClient.post()
                .uri("http://localhost:8000/api/ai/parse-resume")
                .bodyValue(new ResumeParseRequest(text))
                .retrieve()
                .bodyToMono(AIResponse.class)
                .map(AIResponse::data)
                .block();
    }
    /** Step 4: Save resume */
    public Resume saveResume(Resume resume) {
        return repo.save(resume);
    }

    public Resume processAndSave(ParsedResumeDTO parsed, Long userId) {
        Resume resume = resumeMapper.toEntity(parsed, userId);
        System.out.println("after mapping #############");
        System.out.println(resume);
        return saveResume(resume);
    }
    public Resume getByUser(Long userId) {
        return repo.findByUserId(userId);
    }

    public Resume updateResume(Long resumeId, Resume updated) {
        updated.setId(resumeId);
        return repo.save(updated);
    }
}
