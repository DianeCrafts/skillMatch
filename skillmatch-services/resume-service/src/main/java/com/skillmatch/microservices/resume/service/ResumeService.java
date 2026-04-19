package com.skillmatch.microservices.resume.service;

import com.google.gson.Gson;
import com.skillmatch.microservices.resume.ai.AiClient;
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
import org.springframework.beans.factory.annotation.Value;

@Service
public class ResumeService {

    private final ResumeRepository repo;
    private final ResumeMapper resumeMapper;
    private final WebClient webClient;
    private final AiClient aiClient;
    private final String aiServiceUrl;
    public ResumeService(ResumeRepository repo, WebClient aiWebClient, ResumeMapper resumeMapper, AiClient aiClient, @Value("${ai.service.url}") String aiServiceUrl) {
        this.repo = repo;
        this.webClient = aiWebClient;
        this.resumeMapper = resumeMapper;
        this.aiClient = aiClient;
        this.aiServiceUrl = aiServiceUrl;
    }

    /** Step 1: Extract text from PDF/DOCX */
    public String parseResume(MultipartFile file) throws Exception {
        Tika tika = new Tika();
        return tika.parseToString(file.getInputStream());
    }

    /** Step 2: Send extracted text to AIService */
    public ResumeDTO sendToAIService(String text) {
        return webClient.post()
                .uri(aiServiceUrl + "/api/ai/parse-resume")
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

        // Build or reuse fullText
        String fullText = parsed.fullText();
        if (fullText == null || fullText.isBlank()) {
            fullText = buildFullTextFromFields(parsed);
        }

        // Generate embedding
        float[] embedding = aiClient.getEmbedding(fullText);

        // Convert DTO -> entity
        Resume resume = resumeMapper.toEntity(parsed, userId);
        resume.setEmbeddingJson(new Gson().toJson(embedding));

        return repo.save(resume);
    }


    private String buildFullTextFromFields(ResumeDTO dto) {

        StringBuilder sb = new StringBuilder();

        if (dto.summary() != null) sb.append(dto.summary()).append("\n");
        if (dto.name() != null) sb.append(dto.name()).append("\n");
        if (dto.email() != null) sb.append(dto.email()).append("\n");
        if (dto.phone() != null) sb.append(dto.phone()).append("\n");

        if (dto.skills() != null) {
            sb.append("Skills: ").append(String.join(", ", dto.skills())).append("\n");
        }

        if (dto.education() != null) {
            dto.education().forEach(e -> sb.append(
                    e.institution() + " " +
                            e.degree() + " " +
                            e.field() + " " +
                            e.startDate() + " " + e.endDate() + "\n"
            ));
        }

        if (dto.experience() != null) {
            dto.experience().forEach(e -> sb.append(
                    e.company() + " " +
                            e.position() + " " +
                            e.startDate() + " " + e.endDate() + " " +
                            e.description() + "\n"
            ));
        }

        return sb.toString();
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

    public Resume getById(Long resumeId) {
        return repo.findById(resumeId).orElse(null);
    }

    public ResumeDTO attachFullText(ResumeDTO dto, String fullText) {
        return new ResumeDTO(
                dto.id(),
                dto.summary(),
                dto.name(),
                dto.email(),
                dto.phone(),
                dto.education(),
                dto.experience(),
                dto.skills(),
                fullText
        );
    }

}
