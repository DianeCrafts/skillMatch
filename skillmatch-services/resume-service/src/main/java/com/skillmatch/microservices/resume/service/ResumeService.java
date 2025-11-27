package com.skillmatch.microservices.resume.service;

import com.skillmatch.microservices.resume.dto.AIResponse;
import com.skillmatch.microservices.resume.dto.ResumeDTO;
import com.skillmatch.microservices.resume.dto.ResumeParseRequest;
import com.skillmatch.microservices.resume.mapper.ResumeMapper;
import com.skillmatch.microservices.resume.model.*;
import com.skillmatch.microservices.resume.repository.ResumeRepository;
import org.apache.tika.Tika;
import org.springframework.stereotype.Service;
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
    public ResumeDTO sendToAIService(String text) {
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

    public Resume processAndSave(ResumeDTO parsed, Long userId) {
        Resume resume = resumeMapper.toEntity(parsed, userId);
        return saveResume(resume);
    }
    public Resume getByUser(Long userId) {
        return repo.findByUserId(userId);
    }

    public ResumeDTO toDto(Resume resume){
        return resumeMapper.toDTO(resume);
    }

    public Resume updateFromDTO(Long resumeId, ResumeDTO dto) {
        Resume existing = repo.findById(resumeId)
                .orElseThrow(() -> new RuntimeException("Resume not found"));

        resumeMapper.mergeIntoExisting(existing, dto);

        return repo.save(existing);
    }

}
