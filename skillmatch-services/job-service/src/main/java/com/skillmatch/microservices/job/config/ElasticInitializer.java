//package com.skillmatch.microservices.job.config;
//
//import co.elastic.clients.elasticsearch.ElasticsearchClient;
//import jakarta.annotation.PostConstruct;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//import java.io.StringReader;
//
//@Component
//@RequiredArgsConstructor
//public class ElasticInitializer {
//
//    private final ElasticsearchClient esClient;
//
//    @PostConstruct
//    public void createIndexIfNotExists() throws IOException {
//
//        boolean exists = esClient.indices().exists(e -> e.index("jobs")).value();
//
//        if (!exists) {
//            String mapping = """
//            {
//              "settings": {
//                "analysis": {
//                  "analyzer": {
//                    "job_analyzer": {
//                      "type": "custom",
//                      "tokenizer": "standard",
//                      "filter": ["lowercase", "stemmer"]
//                    }
//                  }
//                }
//              },
//              "mappings": {
//                "properties": {
//                  "id": { "type": "long" },
//                  "title": { "type": "text", "analyzer": "job_analyzer" },
//                  "description": { "type": "text", "analyzer": "job_analyzer" },
//                  "company_name": { "type": "text", "analyzer": "job_analyzer" },
//                  "requirements": { "type": "text", "analyzer": "job_analyzer" },
//                  "skills": { "type": "text", "analyzer": "job_analyzer" },
//                  "experience": { "type": "text", "analyzer": "job_analyzer" },
//                  "location": { "type": "text", "analyzer": "job_analyzer" },
//                  "salary": { "type": "keyword" },
//                  "remote": { "type": "boolean" },
//                  "embedding_json": {
//                    "type": "dense_vector",
//                    "dims": 384,
//                    "index": true,
//                    "similarity": "cosine"
//                  }
//                }
//              }
//            }
//            """;
//
//            esClient.indices().create(c -> c.index("jobs").withJson(new StringReader(mapping)));
//        }
//    }
//}
