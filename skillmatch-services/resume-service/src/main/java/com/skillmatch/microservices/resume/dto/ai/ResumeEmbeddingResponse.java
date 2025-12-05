package com.skillmatch.microservices.resume.dto.ai;

public record ResumeEmbeddingResponse(
        String embeddingJson,
        float[] vector
) {}
