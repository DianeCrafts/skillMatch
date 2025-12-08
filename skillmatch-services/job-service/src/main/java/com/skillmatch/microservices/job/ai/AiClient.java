package com.skillmatch.microservices.job.ai;

import com.skillmatch.microservices.job.dto.ai.EmbeddingRequest;
import com.skillmatch.microservices.job.dto.ai.EmbeddingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class AiClient {

    private final WebClient aiWebClient;

    public float[] getEmbedding(String text) {

        EmbeddingRequest request = new EmbeddingRequest(text);

        EmbeddingResponse response = aiWebClient.post()
                .uri("/api/ai/embed-text") // ← NO localhost, NO hardcoded base URL
                .bodyValue(request)
                .retrieve()
                .bodyToMono(EmbeddingResponse.class)
                .block();

        assert response != null;
        return response.embedding();
    }
}

