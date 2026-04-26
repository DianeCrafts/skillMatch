package com.skillmatch.recommendation.dto.response;

import java.util.List;

public record EmbeddingResponse(
        List<Double> embedding
) {}