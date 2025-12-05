package com.skillmatch.microservices.job.ai;

import com.skillmatch.microservices.job.dto.ai.EmbeddingRequest;
import com.skillmatch.microservices.job.dto.ai.EmbeddingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class AiClient {

    private final WebClient webClient;

    public float[] getEmbedding(String text) {
        EmbeddingRequest request = new EmbeddingRequest(text);

        EmbeddingResponse response = webClient.post()
                .uri("http://localhost:8000/api/ai/embed-text")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(EmbeddingResponse.class)
                .block();

        assert response != null;
        return response.embedding();
    }
}
