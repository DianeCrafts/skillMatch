package com.skillmatch.microservices.resume.ai;

import com.skillmatch.microservices.resume.dto.ai.EmbeddingRequest;
import com.skillmatch.microservices.resume.dto.ai.EmbeddingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class AiClient {

    @Qualifier("aiWebClient")
    private final WebClient aiWebClient;

    public float[] getEmbedding(String text) {

        EmbeddingRequest request = new EmbeddingRequest(text);

        EmbeddingResponse response = aiWebClient.post()
                .uri("/api/ai/embed-text")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(EmbeddingResponse.class)
                .block();

        return response.embedding();
    }
}

