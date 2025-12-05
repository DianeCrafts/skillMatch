package com.skillmatch.microservices.job.dto.ai;

public record EmbeddingResponse(String status, float[] embedding) {}