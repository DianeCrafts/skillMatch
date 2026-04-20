package com.skillmatch.file.config;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "app.file")
public class FileStorageProperties {

    @NotBlank
    private String uploadDir;

    private List<String> allowedExtensions = List.of("pdf");

    private List<String> allowedContentTypes = List.of("application/pdf");

    @Min(1)
    private long maxSizeBytes = 5 * 1024 * 1024;
}