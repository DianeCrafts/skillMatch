package com.skillmatch.microservices.resume.model;

import jakarta.persistence.*;

@Entity
public class Experience {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String company;
    private String position;
    private java.sql.Date startDate;
    private java.sql.Date endDate;
    private String description;

    @ManyToOne
    @JoinColumn(name = "resume_id")
    private Resume resume;
}
