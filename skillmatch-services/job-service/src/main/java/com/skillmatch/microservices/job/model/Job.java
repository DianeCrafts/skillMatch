package com.skillmatch.microservices.job.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "jobs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long recruiterId;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String companyName;

    // Requirements
    @ElementCollection
    private List<String> requirements;

    private String location;

    private String salary;

    private String experience;

    // Skills
    @ElementCollection
    private List<String> skills;

    private boolean remote;

    private LocalDateTime createdAt = LocalDateTime.now();


    @Column(columnDefinition = "TEXT")
    private String embeddingJson;
}

