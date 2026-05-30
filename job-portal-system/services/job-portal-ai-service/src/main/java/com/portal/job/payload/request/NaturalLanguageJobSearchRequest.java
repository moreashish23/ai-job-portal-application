package com.portal.job.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NaturalLanguageJobSearchRequest {

    @NotBlank(message = "Search query is required")
    @Size(max = 500, message = "Search query must not exceed 500 characters")
    private String query;
}