package com.skillmatch.recommendation.repository;

import com.skillmatch.recommendation.entity.UserEmbedding;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
public interface UserEmbeddingRepository extends JpaRepository<UserEmbedding, Long> {

    @Modifying
    @Transactional
    @Query(value = """
        INSERT INTO user_embeddings (user_id, profile_text, embedding, updated_at)
        VALUES (:userId, :profileText, CAST(:embedding AS vector), :updatedAt)
        ON CONFLICT (user_id)
        DO UPDATE SET
            profile_text = EXCLUDED.profile_text,
            embedding = EXCLUDED.embedding,
            updated_at = EXCLUDED.updated_at
        """, nativeQuery = true)
    void upsertUserEmbedding(
            @Param("userId") Long userId,
            @Param("profileText") String profileText,
            @Param("embedding") String embedding,
            @Param("updatedAt") LocalDateTime updatedAt
    );
}