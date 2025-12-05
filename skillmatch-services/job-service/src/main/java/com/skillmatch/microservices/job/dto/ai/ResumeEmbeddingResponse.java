package com.skillmatch.microservices.job.dto.ai;

public record ResumeEmbeddingResponse(
        String embeddingJson,
        float[] vector
) {}
