package com.skillmatch.microservices.resume.dto.ai;

public record EmbeddingResponse(String status, float[] embedding) {}