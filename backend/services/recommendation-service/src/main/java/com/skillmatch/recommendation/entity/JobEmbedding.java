package com.skillmatch.recommendation.entity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@Entity
@Table(name = "job_embeddings")
public class JobEmbedding {
    @Id
    @Column(name = "job_id")
    private Long jobId;

    @Column(name = "job_text", nullable = false, columnDefinition = "text")
    private String jobText;

    @Column(name = "embedding", nullable = false, columnDefinition = "vector(384)")
    private String embedding;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}