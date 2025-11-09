package com.skillmatch.microservices.resume.model;

import jakarta.persistence.*;

@Entity
public class Education {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String institution;
    private String degree;
    private String field;
    private java.sql.Date startDate;
    private java.sql.Date endDate;

    @ManyToOne
    @JoinColumn(name = "resume_id")
    private Resume resume;
}
