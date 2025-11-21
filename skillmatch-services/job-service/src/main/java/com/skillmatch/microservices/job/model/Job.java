package com.skillmatch.microservices.job.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "jobs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String recruiterId;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ElementCollection
    private List<String> skillsRequired;

    private String location;

    private boolean remote;

    private LocalDateTime createdAt = LocalDateTime.now();
}
