package com.skillmatch.profile.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateProfileRequest {

    @Size(max = 100)
    private String firstName;

    @Size(max = 100)
    private String lastName;

    @Size(max = 150)
    private String headline;

    @Size(max = 2000)
    private String summary;

    @Size(max = 255)
    private String location;
}