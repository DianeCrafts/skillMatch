package com.skillmatch.microservices.job.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.skillmatch.microservices.job.model.Job;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
                                            .fields("title^3", "description", "requirements", "location", "skills", "experience")
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


    private List<Float> toFloatList(float[] vec) {
        List<Float> list = new ArrayList<>(vec.length);
        for (float f : vec) list.add(f);
        return list;
    }

    public List<Job> searchByVector(float[] userVec) {
        try {
            List<Float> vectorList = toFloatList(userVec);

            SearchResponse<Map> response = esClient.search(s -> s
                            .index("jobs")
                            .knn(kn -> kn
                                    .field("embedding_json")     // your vector field
                                    .queryVector(vectorList)
                                    .k(5)                       // return top 10 results
                                    .numCandidates(50)           // search broader set before filtering top k
                            ),
                    Map.class
            );

            return response.hits().hits().stream()
                    .map(hit -> mapToJob(hit.source()))
                    .toList();

        } catch (Exception e) {
            throw new RuntimeException("Vector search failed: " + e.getMessage(), e);
        }
    }

    private Job mapToJob(Map<String, Object> src) {
        Job job = new Job();
        job.setId(Long.valueOf(src.get("id").toString()));
        job.setTitle((String) src.get("title"));
        job.setDescription((String) src.get("description"));
        job.setSkills((List<String>) src.get("skills"));
        job.setLocation((String) src.get("location"));
        job.setSalary((String) src.get("salary"));
        job.setExperience((String) src.get("experience"));
        job.setRemote((Boolean) src.get("remote"));
        return job;
    }




}