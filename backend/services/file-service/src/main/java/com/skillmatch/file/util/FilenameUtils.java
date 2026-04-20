package com.skillmatch.file.util;

import java.util.UUID;

public final class FilenameUtils {

    private FilenameUtils() {
    }

    public static String extractExtension(String filename) {
        if (filename == null || filename.isBlank()) {
            return "";
        }

        int lastDot = filename.lastIndexOf('.');
        if (lastDot < 0 || lastDot == filename.length() - 1) {
            return "";
        }

        return filename.substring(lastDot + 1).toLowerCase();
    }

    public static String generateStoredFilename(String originalFilename) {
        String extension = extractExtension(originalFilename);
        String uuid = UUID.randomUUID().toString();

        if (extension.isBlank()) {
            return uuid;
        }

        return uuid + "." + extension;
    }
}