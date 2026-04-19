package com.skillmatch.microservices.job.seed;

import com.skillmatch.microservices.job.service.JobSeeder;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
public class DataInitializer implements CommandLineRunner {

    private final JobSeeder jobSeeder;

    public DataInitializer(JobSeeder jobSeeder) {
        this.jobSeeder = jobSeeder;
    }

    @Override
    public void run(String... args) {
        jobSeeder.seedFromFile("jobs-seed.json");
    }
}
