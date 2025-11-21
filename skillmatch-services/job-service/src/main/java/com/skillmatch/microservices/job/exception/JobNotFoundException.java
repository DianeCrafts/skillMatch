package com.skillmatch.microservices.job.exception;

public class JobNotFoundException extends RuntimeException {
    public JobNotFoundException(String id) {
        super("Job not found: " + id);
    }
}