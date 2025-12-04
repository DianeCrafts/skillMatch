package com.skillmatch.microservices.job.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.skillmatch.microservices.job.model.Job;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobSearchService {

    private final ElasticsearchClient esClient;

    public JobSearchService(ElasticsearchClient esClient) {
        this.esClient = esClient;
    }

    public List<Job> searchJobs(String keyword) {
        try {
            SearchResponse<Job> response = esClient.search(s -> s
                            .index("jobs")
                            .query(q -> q
                                    .multiMatch(mm -> mm
                                            .fields("title^3", "description", "requirements")
                                            .query(keyword)
                                    )
                            ),
                    Job.class
            );

            return response.hits().hits().stream()
                    .map(Hit::source)
                    .collect(Collectors.toList());

        } catch (IOException e) {
            throw new RuntimeException("Failed to search jobs", e);
        }
    }
}