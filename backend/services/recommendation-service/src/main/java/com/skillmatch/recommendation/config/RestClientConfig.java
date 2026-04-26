package com.skillmatch.recommendation.config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;


@Configuration
public class RestClientConfig {

    @Value("${app.services.profile-service-url}")
    private String profileServiceUrl;

    @Value("${app.services.job-service-url}")
    private String jobServiceUrl;

    @Value("${app.services.embedding-service-url}")
    private String embeddingServiceUrl;

    @Bean
    public RestClient profileRestClient() {
        return RestClient.builder()
                .baseUrl(profileServiceUrl)
                .build();
    }

    @Bean
    public RestClient jobRestClient() {
        return RestClient.builder()
                .baseUrl(jobServiceUrl)
                .build();
    }

    @Bean
    public RestClient embeddingRestClient() {
        return RestClient.builder()
                .baseUrl(embeddingServiceUrl)
                .build();
    }
}