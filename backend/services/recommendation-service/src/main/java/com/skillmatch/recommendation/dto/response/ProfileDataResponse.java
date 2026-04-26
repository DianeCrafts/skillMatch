package com.skillmatch.recommendation.dto.response;

import java.util.List;

public record ProfileDataResponse(
        Long userId,
        String summary,
        List<SkillDto> skills,
        List<ExperienceDto> experiences
) {}

