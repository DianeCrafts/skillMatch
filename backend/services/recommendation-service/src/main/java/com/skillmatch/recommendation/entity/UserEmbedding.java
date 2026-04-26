package com.skillmatch.recommendation.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;


import java.time.LocalDateTime;
@Setter
@Getter
@Entity
@Table(name = "user_embeddings")
public class UserEmbedding {
    @Id
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "profile_text", nullable = false, columnDefinition = "text")
    private String profileText;

    @Column(name = "embedding", nullable = false, columnDefinition = "vector(384)")
    private String embedding;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}