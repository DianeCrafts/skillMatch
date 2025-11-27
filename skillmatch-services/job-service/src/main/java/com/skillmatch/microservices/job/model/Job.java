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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long recruiterId;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    // Requirements
    @ElementCollection
    private List<String> requirements;

    private String location;

    private String salary;       // Added

    private String experience;   // Added

    // Skills
    @ElementCollection
    private List<String> skills;

    private boolean remote;

    private LocalDateTime createdAt = LocalDateTime.now();
}

