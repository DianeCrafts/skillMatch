package com.skillmatch.recommendation.repository;

import com.skillmatch.recommendation.entity.JobEmbedding;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

public interface JobEmbeddingRepository extends JpaRepository<JobEmbedding, Long> {

    @Query(value = """
        SELECT 
            je.job_id,
            1 - (je.embedding <=> CAST(:userEmbedding AS vector)) AS similarity_score
        FROM job_embeddings je
        ORDER BY je.embedding <=> CAST(:userEmbedding AS vector)
        LIMIT :limit
        """, nativeQuery = true)
    List<Object[]> findMostSimilarJobs(
            @Param("userEmbedding") String userEmbedding,
            @Param("limit") int limit
    );

    @Modifying
    @Transactional
    @Query(value = """
    INSERT INTO job_embeddings (job_id, job_text, embedding, updated_at)
    VALUES (:jobId, :jobText, CAST(:embedding AS vector), :updatedAt)
    ON CONFLICT (job_id)
    DO UPDATE SET
        job_text = EXCLUDED.job_text,
        embedding = EXCLUDED.embedding,
        updated_at = EXCLUDED.updated_at
    """, nativeQuery = true)
    void upsertJobEmbedding(
            @Param("jobId") Long jobId,
            @Param("jobText") String jobText,
            @Param("embedding") String embedding,
            @Param("updatedAt") LocalDateTime updatedAt
    );
}