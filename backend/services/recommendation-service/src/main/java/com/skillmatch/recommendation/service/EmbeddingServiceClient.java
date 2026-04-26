package com.skillmatch.recommendation.service;

import com.skillmatch.recommendation.exception.ExternalServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
@Service
@RequiredArgsConstructor
public class EmbeddingServiceClient {

    @Value("${app.services.embedding-service-url}")
    private String embeddingServiceUrl;
    private final ObjectMapper objectMapper = new ObjectMapper();
    public List<Double> generateEmbedding(String text) {
        try {
            String encodedText = URLEncoder.encode(text, StandardCharsets.UTF_8);
            String url = embeddingServiceUrl + "/api/embeddings?text=" + encodedText;

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Accept", "application/json")
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpResponse<String> response = HttpClient.newHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() >= 400) {
                throw new ExternalServiceException("Embedding service failed: " + response.body());
            }

            String body = response.body();

            JsonNode root = objectMapper.readTree(body);
            JsonNode embeddingNode = root.get("embedding");

            List<Double> embedding = new ArrayList<>();

            for (JsonNode value : embeddingNode) {
                embedding.add(value.asDouble());
            }

            return embedding;

        } catch (Exception e) {
            e.printStackTrace();
            throw new ExternalServiceException("Embedding service failed");
        }
    }
}