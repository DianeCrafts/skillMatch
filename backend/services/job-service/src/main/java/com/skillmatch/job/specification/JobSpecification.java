package com.skillmatch.job.specification;

import com.skillmatch.job.entity.Job;
import com.skillmatch.job.entity.JobStatus;
import com.skillmatch.job.entity.EmploymentType;
import com.skillmatch.job.entity.ExperienceLevel;
import org.springframework.data.jpa.domain.Specification;

public class JobSpecification {

    public static Specification<Job> isPublished() {
        return (root, query, cb) ->
                cb.equal(root.get("status"), JobStatus.PUBLISHED);
    }

    public static Specification<Job> keywordContains(String keyword) {
        return (root, query, cb) -> {
            if (keyword == null || keyword.trim().isEmpty()) {
                return cb.conjunction();
            }

            String likePattern = "%" + keyword.toLowerCase() + "%";

            return cb.or(
                    cb.like(cb.lower(root.get("title")), likePattern),
                    cb.like(cb.lower(root.get("description")), likePattern)
            );
        };
    }

    public static Specification<Job> locationEquals(String location) {
        return (root, query, cb) -> {
            if (location == null || location.trim().isEmpty()) {
                return cb.conjunction();
            }

            return cb.equal(
                    cb.lower(root.get("location")),
                    location.toLowerCase()
            );
        };
    }

    public static Specification<Job> employmentTypeEquals(EmploymentType type) {
        return (root, query, cb) -> {
            if (type == null) {
                return cb.conjunction();
            }

            return cb.equal(root.get("employmentType"), type);
        };
    }

    public static Specification<Job> experienceLevelEquals(ExperienceLevel level) {
        return (root, query, cb) -> {
            if (level == null) {
                return cb.conjunction();
            }

            return cb.equal(root.get("experienceLevel"), level);
        };
    }
}